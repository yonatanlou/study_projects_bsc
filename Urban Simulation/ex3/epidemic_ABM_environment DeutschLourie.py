from random import random, choice, randint, seed
import geopandas
import networkx as nx
from copy import deepcopy


class Building:    
    # initialize building
    def __init__(self, geometry, landuse):
        self.geometry = geometry
        self.landuse = landuse
    
    def distance(self, n): # compute the distance between buildings and another building n
        # Euclidean distance - sqare root of (squared x differece + squared y difference)
        return ((self.geometry.x - n.geometry.x)**2 + (self.geometry.y - n.geometry.y)**2)**0.5
        
        
class Agent:
    # initialize agent
    def  __init__(self, status, budget, social_position, home, utility_value,risk):
        self.status = status # epidemiological status
        self.budget = budget # travel budget
        self.social_position = social_position # social position
        self.home = home # home building
        self.utility_value = utility_value # minimal utility value
        self.risk = risk
        self.location = home # set current location to home
    
    def compute_utility(self, agent): # compute utility from interaction with agent
        d = self.location.distance(agent.location) # compute distance between current locations
        if d > self.budget: # if distance is greater than budget, return 0 utility
            return 0
        d_social = abs(self.social_position - agent.social_position) # social distance
        d_costs = d/self.budget # relative utility from distance travelled
        utility = model.w1 * (1-d_social) + (1-model.w1) * (1-d_costs) # compute utility
        return utility
    
    # find candidates to choose from when moving
    def find_candidates(self):
        candidates = []
        for a1 in nx.generators.ego_graph(model.network, self, radius=3):
            if self != a1 and self.compute_utility(a1) >= self.utility_value:
                candidates.append(a1)
        return list(set(candidates))
        
    # agent action
    def mobility_model(self): # move agents in space
        statuses = [(a.status == 0 or a.status > model.time_to_recover) for a in model.network.neighbors(self)]
        if 1 <= self.status <= model.time_to_recover: # if agent if infected
            self.location = self.home # make sure agent is at home
        
        elif sum(statuses)/len(statuses) >= self.risk: # if agent if infected
            self.location = self.home # make sure agent is at home
        
        elif random() > 0.5:
            self.location = choice(model.non_residential)
        else: # if at mandatory activity - go visit friends
            candidates = self.find_candidates()
            if len(candidates) > 0: # if there are such neighbors - choose one and move to its location
                c = choice(candidates) 
                self.location = c.location
            else: # if there are none - go back home
                self.location = self.home
                
    def contagion_model(self):        
        if 1 <= self.status <= model.time_to_recover: # if infected - increase status by 1
            self.status += 1
        elif self.status == 0: 
            # find all other agents within the same building
            choice_set = [a for a in model.network.nodes if a != self and a.location == self.location]
            if len(choice_set) > 0: # if there are such
                a = choice(choice_set) # randomly choose an agent
                a_idx = list(model.network.nodes).index(a)
                a_copy_status = model.nodes_copy[a_idx].status
                if self.status == 0 and 1 <= a_copy_status <= model.time_to_recover: # if a is infected
                    self.status = 1 # infect agent


class Model:    
    def __init__(self, agents_num, time_to_recover, w1, connections, blds,risk):
        self.time_to_recover = time_to_recover
        self.w1 = w1
        self.risk = risk
        #self.network = nx.Graph()
        self.network = nx.DiGraph() # create a network element
        self.buildings = []
        
        for idx, row in blds.iterrows(): # iterate through records
            self.buildings.append(Building(row.geometry, row.landuse)) # append building with geometry and landuse
        
        self.non_residential = [b for b in self.buildings if b.landuse=='commercial']
        residential = [b for b in self.buildings if b.landuse=='residential']
        for i in range(agents_num):
            # create agent with attributes status, home, activity location, social position, and utility value
            agent = Agent((random() < 0.05) * 1, randint(200, 800), random(), choice(residential), random(),risk) 
            self.network.add_node(agent) # add agent as node to the network
        
        for a in self.network.nodes: # for every agent = for every node
            candidates = [f for f in self.network.nodes if f != a] # find all agents that are not a
            distances = [a.home.distance(f.home) for f in candidates] # compute distance from agents
            sorted_distances = sorted(distances) # sort distances from lowest to highest
            ranks = [sorted_distances.index(d) for d in distances] # get a rank score for each distance
            # choose only agents whose rank is lower than connections
            friends = [candidates[i] for i in range(len(candidates)) if ranks[i] < connections]
            for f in friends:
                self.network.add_edge(a, f) # add an edge to the network between agent and friend
        l = list(self.network.edges)
        for i in l:
            node = i[0]
            n = random()
            if n < 0.1:
                c = [node1 for node1 in self.network.nodes if node1 != node and node1 not in self.network.neighbors(node)]
                node1 = choice(c)
                self.network.add_edge(node,node1)
                self.network.remove_edge(i[0],i[1])
                
                        
        
    def simulate(self):
        # compute number of infected agents
        infected = len([a for a in self.network.nodes if 1 <= a.status <= self.time_to_recover])
        t = 1 # store number of iteration
        max_infected = infected # variable for storing maximal number of infected
        max_t = t # variable for storing the time of maximum
        while infected > 0: # stop rule of simulation - no more infected cells
            self.nodes_copy = deepcopy(list(self.network.nodes))
            for a in self.network.nodes:
                a.mobility_model() # move
                a.contagion_model() # check infection
    
            # compute number of infected agents
            infected = len([a for a in self.network.nodes if 1 <= a.status <= self.time_to_recover])
            if infected > max_infected: # if number of infected is more than maximum
                max_infected = infected # update maximum
                max_t = t # update time of maximum
            t += 1
            
        total_infected = len([a for a in self.network.nodes if a.status > self.time_to_recover])
        return [total_infected, max_infected, max_t, t] # return maximum, time of max, length of simulation


time_to_recover = 12
shape = 'bldgs_points.shp'
blds = geopandas.read_file(shape) # read shapefile
agents_num = 900
seed(1)
risk = random()
#risk = 0.6071536265229532 
for connections in [2,6,10]:
    for w1 in [0.1, 0.5, 0.9]:
        results = [] # store outputs of all simulations
        for i in range(5):
            model = Model(agents_num, time_to_recover, w1, connections, blds,risk)
            results.append(model.simulate()) # store outputs
            
        # compute average over simulations
        avg_results = [sum([results[i][j] for i in range(5)]) / 5 for j in range(4)]
        print(risk,connections, w1, avg_results)
