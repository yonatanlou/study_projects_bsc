from random import random, choice, randint, uniform
from copy import deepcopy
import pandas as pd



class Agent:
    # agent methods
    # initialize agent
    def __init__(self, status, x, y, budget, social_position):
        self.status = status
        self.x = x
        self.y = y
        self.budget = budget
        self.social_position = social_position
        if social_position < 0.5:
            self.distancing = uniform(0.25, 0.75)
        else:
            self.distancing = 1

    def compute_utillity(self, agent):
      d = self.distance(agent)
      d_costs = d / self.budget

      if d > self.budget:
          return 0
      d_social = abs(self.social_position - agent.social_position)
      utility = (model.w1 * (1 - d_social) * self.distancing) + (1 - model.w1) * (1 - d_costs)
      return utility

    # distance function
    def distance(self, a):
        d_x = (self.x - a.x) ** 2
        d_y = (self.y - a.y) ** 2
        return (d_x + d_y) ** 0.5

    # agent action
    def step(self):
        if 1 <= self.status <= model.time_to_recover:  # if agent if infected
            self.status += 1  # increase status by 1
        elif self.status == 0:  # if agent is healthy
            choice_set = [agent for agent in model.agents_copy if self != agent and self.compute_utillity(agent) > 0.5]
            if len(choice_set) > 0:
                for i in range(model.interactions):  # repeat according to interactions value
                    a = choice(choice_set)  # choose a random agent
                    if 1 <= a.status <= model.time_to_recover:  # if a is infected
                        self.status += 1  # infected agent
                        break


class Model:
    # methods
    def __init__(self, agents_num, interactions, time_to_recover, w1):
        self.agents_num = agents_num
        self.interactions = interactions
        self.time_to_recover = time_to_recover
        self.w1 = w1

        # create the agents set
        self.agents = [Agent((random() < 0.05) * 1, randint(1, 30), randint(1, 30), randint(1, 15), random()) for i in
                       range(self.agents_num)]

    def simulate(self):
        # compute number of infected agents
        infected = len([a for a in self.agents if 1 <= a.status <= self.time_to_recover])
        t = 1  # store number of iteration
        max_infected = infected  # variable for storing maximal number of infected
        max_t = t  # variable for storing the time of maximum
        while infected > 0:  # stop rule of simulation - no more infected cells
            self.agents_copy = deepcopy(self.agents)
            # iterate over all agents
            for a in self.agents:
                a.step()

            # compute number of infected cells
            infected = len([a for a in self.agents if 1 <= a.status <= self.time_to_recover])
            social_position = ([a.social_position for a in self.agents if 1 <= a.status <= self.time_to_recover])
            if infected > max_infected:  # if number of infected is more than maximum
                max_infected = infected  # update maximum
                max_t = t  # update time of maximum
            t += 1
        total_infected = len([a for a in self.agents if a.status > self.time_to_recover])
        social_position_array = ([a.social_position for a in self.agents if a.status > self.time_to_recover])
        social_position_mean = sum(social_position_array)/len(social_position_array)

        return [total_infected, max_infected, max_t, t, round(social_position_mean,3)]  # return maximum, time of max, length of simulation


final_results = []
agents_num = 900
interactions = 3
w1 = [0, 0.25, 0.5, 0.75, 1]
for time_to_recover in [2, 6]:
    for w in w1:
        results = []  # store outputs of all simulations
        for i in range(5):  # simulate 5 times
            model = Model(agents_num, interactions, time_to_recover, w)
            results.append(model.simulate())  # store outputs
        # compute average over simulations

        avg_results = [sum([results[i][j] for i in range(5)]) / 5 for j in range(5)]
        print(w, time_to_recover, avg_results)
        final_results.append([w, time_to_recover, *avg_results])



col_names = ["w1 ערך", "time_to_recover ערך", "מס חולים ממוצע", "מס חולים מקסימלי ממוצע", "מועד המקסימום ממוצע", "משך סימולציה ממוצע", "סטטוס חברתי ממוצע"]
df = pd.DataFrame(data=final_results, columns=col_names)
df = df[df.columns[::-1]]
df.to_excel("output.xlsx")