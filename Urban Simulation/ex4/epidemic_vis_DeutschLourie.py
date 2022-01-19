from random import random, choice, randint
import geopandas
import networkx as nx
from copy import deepcopy
import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns


class Building:
    # initialize building
    def __init__(self, geometry, landuse):
        self.geometry = geometry
        self.landuse = landuse

    def distance(self, n):  # compute the distance between buildings and another building n
        # Euclidean distance - sqare root of (squared x differece + squared y difference)
        return ((self.geometry.x - n.geometry.x) ** 2 + (self.geometry.y - n.geometry.y) ** 2) ** 0.5


class Agent:
    # initialize agent
    def __init__(self, status, budget, social_position, home, utility_value):
        self.status = status  # epidemiological status
        self.budget = budget  # travel budget
        self.social_position = social_position  # social position
        self.home = home  # home building
        self.utility_value = utility_value  # minimal utility value

        self.location = home  # set current location to home

    def compute_utility(self, agent):  # compute utility from interaction with agent
        d = self.location.distance(agent.location)  # compute distance between current locations
        if d > self.budget:  # if distance is greater than budget, return 0 utility
            return 0
        d_social = abs(self.social_position - agent.social_position)  # social distance
        d_costs = d / self.budget  # relative utility from distance travelled
        utility = model.w1 * (1 - d_social) + (1 - model.w1) * (1 - d_costs)  # compute utility
        return utility

    # find candidates to choose from when moving
    def find_candidates(self):
        candidates = []
        for a1 in nx.generators.ego_graph(model.network, self, radius=3):
            if self != a1 and self.compute_utility(a1) >= self.utility_value:
                candidates.append(a1)
        return list(set(candidates))

    # agent action
    def mobility_model(self):  # move agents in space
        if 1 <= self.status <= model.time_to_recover:  # if agent if infected
            self.location = self.home  # make sure agent is at home
            model.at_home += 1
        elif random() > 0.5:
            self.location = choice(model.non_residential)
        else:  # if at mandatory activity - go visit friends
            candidates = self.find_candidates()
            if len(candidates) > 0:  # if there are such neighbors - choose one and move to its location
                c = choice(candidates)
                model.utility.append(self.compute_utility(c))
                self.location = c.location
            else:  # if there are none - go back home
                self.location = self.home
                model.at_home += 1

    def contagion_model(self):
        if 1 <= self.status <= model.time_to_recover:  # if infected - increase status by 1
            self.status += 1
        elif self.status == 0:
            # find all other agents within the same building
            choice_set = [a for a in model.network.nodes if a != self and a.location == self.location]
            if len(choice_set) > 0:  # if there are such
                a = choice(choice_set)  # randomly choose an agent
                a_idx = list(model.network.nodes).index(a)
                a_copy_status = model.nodes_copy[a_idx].status
                if self.status == 0 and 1 <= a_copy_status <= model.time_to_recover:  # if a is infected
                    self.status = 1  # infect agent


class Model:
    def __init__(self, agents_num, time_to_recover, w1, connections, blds, simulation):
        self.time_to_recover = time_to_recover
        self.w1 = w1
        self.connections = connections
        self.simulation = simulation

        self.network = nx.Graph()  # create a network element
        self.buildings = []
        self.data = []

        for idx, row in blds.iterrows():  # iterate through records
            self.buildings.append(Building(row.geometry, row.landuse))  # append building with geometry and landuse

        self.non_residential = [b for b in self.buildings if b.landuse == 'commercial']
        residential = [b for b in self.buildings if b.landuse == 'residential']
        for i in range(agents_num):
            # create agent with attributes status, home, activity location, social position, and utility value
            agent = Agent((random() < 0.05) * 1, randint(200, 800), random(), choice(residential), random())
            self.network.add_node(agent)  # add agent as node to the network

        for a in self.network.nodes:  # for every agent = for every node
            candidates = [f for f in self.network.nodes if f != a]  # find all agents that are not a
            distances = [a.home.distance(f.home) for f in candidates]  # compute distance from agents
            sorted_distances = sorted(distances)  # sort distances from lowest to highest
            ranks = [sorted_distances.index(d) for d in distances]  # get a rank score for each distance
            # choose only agents whose rank is lower than connections
            friends = [candidates[i] for i in range(len(candidates)) if ranks[i] < connections]
            for f in friends:
                self.network.add_edge(a, f)  # add an edge to the network between agent and friend

    def simulate(self):
        # compute number of infected agents
        active_infected = len([a for a in self.network.nodes if 1 <= a.status <= self.time_to_recover])
        t = 1  # store number of iteration
        while active_infected > 0:  # stop rule of simulation - no more infected cells
            self.utility = []
            self.at_home = 0
            self.nodes_copy = deepcopy(list(self.network.nodes))
            for a in self.network.nodes:
                a.mobility_model()  # move
                a.contagion_model()  # check infection

            # compute number of infected agents
            active_infected = len([a for a in self.network.nodes if 1 <= a.status <= self.time_to_recover])
            new_infected = len([a for a in self.network.nodes if 1 == a.status])
            total_infected = len([a for a in self.network.nodes if 1 <= a.status])
            mean_utility = sum(self.utility) / len(self.utility)
            self.data.append([self.w1, self.connections, self.simulation, t,
                              new_infected, active_infected, total_infected, mean_utility, self.at_home])
            t += 1


time_to_recover = 12
shape = 'bldgs_points.shp'
blds = geopandas.read_file(shape) # read shapefile
agents_num = 900
results = []
for connections in [2,6,10]:
    for w1 in [0.1, 0.5, 0.9]:
        for i in range(5):
            print(connections, w1, i)
            model = Model(agents_num, time_to_recover, w1, connections, blds, i)
            model.simulate()
            for l in model.data:
                results.append(l)

## 1.
df = pd.DataFrame(results, columns=['Price weight', 'Integration', 'Simulation', 'Time',
                                    'New infected', 'Active infected', 'Total infected', 'Mean utility', 'At home'])

##2.
agg_functions = {'New infected':'max', 'Active infected':'max', 'Total infected':'last', 'Mean utility':'mean',
          'Time':'max'}

agg = df.groupby(['Simulation']).agg(agg_functions)


##3.
agg = df.groupby(['Price weight', 'Integration', 'Simulation']).agg(agg_functions)
mean = agg.groupby(['Price weight', 'Integration']).mean()


values = ['New infected', 'Active infected', 'Total infected', 'Mean utility','Time']
def q4(value):
  ##4.a
  piv = pd.pivot(mean.reset_index(), "Price weight", "Integration", value)
  ##4.b
  fig, ax = plt.subplots()
  ##4.c
  sns.heatmap(piv, ax=ax)
  ##4.d
  ax.set_title(f"Price weight vs Integration in terms of {value}")
  ##4.h
  fig.savefig(f"vis/heatmap_{value}.png")

for value in values:
  q4(value)


#5
values_5 = ['New infected', 'Active infected', 'Total infected', 'Mean utility']
def q5(value):
  ##5.a
  fig, ax = plt.subplots()
  ##5.b
  sns.lineplot(data=df, x="Time", y=value, hue="Price weight", style="Integration", ax=ax)
  ##5.c
  ax.set_title(f"{value}")
  ##5.d
  fig.savefig(f"vis/lineplot_{value}.png")
  # fig.show()

for value in values_5:
  q5(value)