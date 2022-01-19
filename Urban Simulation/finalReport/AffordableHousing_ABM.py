from random import choice, randint, random
import pandas as pd

class Asset:
    def __init__(self, rooms, price, neighborhood, affordable):
        self.rooms = rooms
        self.price = price
        self.neighborhood = neighborhood
        self.affordable = affordable
        
        self.resident = None
        self.occupation = None
    
    def compute_price(self):
        # for non-affordable assets - compute the ratio of occupied neighboring assets
        # divide this by the same data from previous iteration and multiply price by the result
        # save occupation ratio to occupation variable of asset
        if self.affordable == 0: # if this asset is not an affordable unit
            neighbors = [asset for asset in model.assets if asset!=self and asset.neighborhood == self.neighborhood]
            occupied = len([asset for asset in neighbors if asset.resident is not None]) / len(neighbors)
            occupation_rate = occupied / self.occupation
            self.price = self.price * occupation_rate
            self.occupation = occupied


class Household:
    def __init__(self, size, social_group, income, home):
        self.size = size
        self.social_group = social_group
        self.income = income
        self.home = home

    def compute_utility(self, asset): 
        # if household cannot afford asset or asset is too small - return 0
        # else - identify all neighbors (if there are none return 1) 
        # compute similarity in terms of suze, ethnicity, and income
        # return utility based on a linear combination of utilities
        if asset.price > self.income/3 or asset.rooms < self.size-1:
            return 0
        neighbors = [a.resident for a in model.assets if a.neighborhood==asset.neighborhood and a.resident is not None and a.resident != self]
        if len(neighbors) == 0:
            return 1
        # TODO 1: compute similarities (size, group, income) and utility
        size_similarity = len([a for a in neighbors if 
                        ((a.size - self.size) <= 1) |((a.size - self.size) >= -1)])/len(neighbors)
        group_similarity = len([a for a in neighbors if a.social_group == self.social_group])/len(neighbors)
        income_similarity = abs(sum([a.income for a in neighbors])/len(neighbors))
        utility = w1 * size_similarity + w2 * group_similarity + (1-w1-w2)*(1-income_similarity)
        return utility
        
    
    def migrate(self):
        # if utility from current home is too low - find all unoccupied assets that provide enough utility
        # if there are such assets - choose one randomly and move to it
        utility = self.compute_utility(self.home)
        if utility <= model.utility_threshold:
            candidates = [asset for asset in model.assets if asset.resident is None and self.compute_utility(asset) > model.utility_threshold]
            if len(candidates) > 0:
                # TODO 2: find new home and update residence accordingly
                new_home = choice(candidates)
                self.home.resident = None
                self.home = new_home
                self.home.resident = self
                model.moves += 1 # add to the count of movements


class Model:
    def __init__(self, assets_gdf, w1, w2, utility_threshold, simulation):
        self.w1 = w1
        self.w2 = w2
        self.utility_threshold = utility_threshold
        self.assets_gdf = assets_gdf
        self.simulation = simulation
        
        self.num_agents = 500
        self.t = 0 # tracks time
        self.results = [] # used to store results
    
    def create_world(self):
        # create assets based on gdf data
        self.assets = []
        for idx, row in self.assets_gdf.iterrows():
            self.assets.append(Asset(row.rooms, row.price, row.neighborh, row.affordable))
        
        # create agents - create an agent and find all non-affordable unoccupied assets that provide enough utility
        # if there are such - choose one randomly; otherwise - choose randomly from all unoccupied
        self.residents = []
        available_assets = [asset for asset in self.assets if asset.affordable==0]
        for i in range(self.num_agents): # for every agents
            hh = Household(randint(1, 5), choice([0,1]), random(), None)
            candidates = [j for j in range(len(available_assets)) if hh.compute_utility(available_assets[j]) > self.utility_threshold] # list of indices
            if len(candidates) > 0:
                home = choice(candidates)
            else:
                home = randint(0, len(available_assets)-1)
            hh.home = available_assets[home]
            hh.home.resident = hh
            self.residents.append(hh)
            del available_assets[home] # delete the chosen asset from available assets
        
        # compute current occupation rate in each of the assets' neighborhoods and save as a trait of the asset
        for asset in self.assets:
            if asset.affordable == 0:
                neighbors = [a for a in model.assets if asset!=a and asset.neighborhood == a.neighborhood]
                occupied = len([a for a in neighbors if a.resident is not None]) / len(neighbors)
                asset.occupation = occupied
    
    def compute_outputs(self): # save outputs for each iteration
        # compute mean utility over agents, mean price over assets, mean homogeneity over neighborhoods
        # if time>0 - compute number of residents in affordable housing, homogeneity in the neighborhood, and average income of residents of affordable housing
        # store within list of results
        mean_utility = sum([hh.compute_utility(hh.home) for hh in self.residents]) / self.num_agents
        
        mean_price = sum([asset.price for asset in self.assets if asset.affordable == 0]) / len([asset.price for asset in self.assets if asset.affordable == 0])
        
        mean_homogeneity = []
        for n in range(1, 11):
            n_assets = [asset for asset in self.assets if asset.neighborhood==n and asset.resident is not None]
            if len(n_assets) > 0: 
                n_homogeneity = len([asset for asset in n_assets if asset.resident.social_group==1]) / len(n_assets)
                if n_homogeneity < 0.5: # make sure the higher share value is stored
                    n_homogeneity = 1 - n_homogeneity
                mean_homogeneity.append(n_homogeneity)
        mean_homogeneity = sum(mean_homogeneity) / len(mean_homogeneity)
        
        if self.t > 0: 
            affordable_residents = [hh for hh in self.residents if hh.home.affordable]
            homogeneity = len([hh for hh in affordable_residents if hh.social_group==1]) / len(affordable_residents)
            if homogeneity < 0.5: # store the higher share
                homogeneity = 1 - homogeneity
            mean_affordable_income = sum([hh.income for hh in affordable_residents]) / len(affordable_residents)
            self.results.append([self.simulation, self.t, self.moves, mean_utility, mean_price, mean_homogeneity,
                                 len(affordable_residents), homogeneity, mean_affordable_income])
        else: # initial state - 0 residents, none values for other outputs relating to neighborhood 10
            self.results.append([self.simulation, self.t, 0, mean_utility, mean_price, mean_homogeneity, None, None, None]) 
                
    
    def simulate(self):
        self.compute_outputs() # save outputs for initial state
        
        for i in range(20): # run simulation for 20 years
            self.t = i+1
            self.moves = 0
            for hh in self.residents: # household step
                hh.migrate()
            
            for asset in self.assets: # assets step
                asset.compute_price()
            
            self.compute_outputs() # save outputs for iteration i


w1 = 0.3
w2 = 0.4
utility_threshold = 0.5
import geopandas as gpd
# TODO 3: import shapefile as geopandas dataframe
shape = 'azeka_heights.shp'
assets_gdf = gpd.read_file(shape)
outputs = []
for i in range(20):
    print(i, end=",")
    model = Model(assets_gdf, w1, w2, utility_threshold, i)
    model.create_world()
    model.simulate()
    for l in model.results:
        outputs.append(l)

df = pd.DataFrame(outputs, columns=['simulation', 'time', 'moves', 'mean_utility', 'mean_price', 'mean_homogeneity',
                                    'affordable_residents', 'affordable_homogeneity', 'affordable_income'])

# TODO 4: create lineplot figures for moves, mean price, mean homogeneity, affordable residents, affordable homogeneity, affordable income
from matplotlib import pyplot as plt
import seaborn as sns
values = ['moves', 'mean_price', 'mean_homogeneity','mean_utility',
        'affordable_residents', 'affordable_homogeneity', 'affordable_income']
def to(value):
    fig, ax = plt.subplots()
    sns.lineplot(data=df, x="time", y=value, ax=ax)
    ax.set_title(f"{value}")
    fig.savefig(f"vis/lineplot_{value}.png")
    # fig.show()
for i in values:
    to(i)
