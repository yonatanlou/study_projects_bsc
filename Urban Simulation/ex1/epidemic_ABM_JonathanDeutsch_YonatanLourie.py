from random import random, choice, randint
from copy import deepcopy


class Agent:
    # agent methods
    # initialize agent
    def  __init__(self, status, x_coord,y_coord):
        self.status = status
        self.x_coord = x_coord
        self.y_coord = y_coord
    # agent action
    def step(self):
        neighbors = [a for a in model.agents_copy if 
                     ((self.x_coord - a.x_coord)**2 + (self.y_coord - a.y_coord)**2)**0.5 <= model.buffer_distance
                     and self != a]
        if 1<= self.status <= model.time_to_recover: # if agent if infected
            self.status += 1 # increase status by 1
        elif self.status == 0: # if agent is healthy
            for i in range(model.interactions): # repeat according to interactions value
                a = choice(neighbors) # choose a random agent
                if 1 <= a.status <= model.time_to_recover: # if a is infected
                    self.status += 1 # infected agent
                    break


class Model:
    # methods
    def __init__(self, agents_num, interactions, time_to_recover, buffer_distance):
        self.agents_num = agents_num
        self.interactions = interactions
        self.time_to_recover = time_to_recover
        self.buffer_distance = buffer_distance
        # create the agents set
        self.agents = [Agent((random() < 0.05) * 1,randint(1,30),randint(1,30)) for i in range(self.agents_num)]
        self.output = []
    
    def simulate(self):
        # compute number of infected agents
        infected = len([a for a in self.agents if 1 <= a.status <= self.time_to_recover])
        #print("infected:",infected)
        t = 1 # store number of iteration
        while infected > 0: # stop rule of simulation - no more infected cells
            self.agents_copy = deepcopy(self.agents)
            # iterate over all agents
            for a in self.agents:
                a.step()
            
            # compute number of infected cells
            infected = len([a for a in self.agents if 1 <= a.status <= self.time_to_recover])
            #print(t, ": infected:",infected)
            self.output.append(infected)
            t += 1
interactions = 3
## MAIN ##
for buffer_distance in [1,5]:
    for time_to_recover in [2,6,12]:
        for k in range(5):
            model = Model(900, interactions, time_to_recover,buffer_distance)
            model.simulate()
            total_infected = len([a for a in model.agents if a.status > time_to_recover])
            max_infected = max(model.output)
            max_time = model.output.index(max_infected) + 1
            duration = len(model.output)
            print(interactions,buffer_distance, time_to_recover,
                  total_infected, max_infected, max_time, duration)

