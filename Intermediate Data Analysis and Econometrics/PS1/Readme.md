PS\_1
================
Yonatan-Lourie
4/12/2021

``` r
library(magrittr)
library(haven)
library(Hmisc)
library(tidyverse)
library(ggplot2)
library(magrittr)
library(dplyr)
library(stargazer)
library(plyr)
library(ri)
library(scales)
```

## Part 1. Setup and Descriptive Statistics

### 1-2

``` r
#1
df_ <- readRDS("earlytrainingproject_clean.rds") #Load the dataset.

#2
varsChild <- c("iq5", "iq6", "iq12", "retn12", "iep12")
varsTeen <- c("iq15", "hsgrad", "parent")
varsAdult <- c("college", "employed", "convicted", "felon", "jailed", "marijuana")

vars <- c(varsChild, varsTeen, varsAdult)
```

### 3-4

``` r
#3
df_ <- df_[c("SUBJECT", "DC_TRT", "SEX", vars)]

#4
print(describe(df_))
```

    ## df_ 
    ## 
    ##  17  Variables      102  Observations
    ## --------------------------------------------------------------------------------
    ## SUBJECT 
    ##        n  missing distinct     Info     Mean      Gmd      .05      .10 
    ##      102        0      102        1     5301    187.4     5102     5107 
    ##      .25      .50      .75      .90      .95 
    ##     5122     5262     5424     5515     5520 
    ## 
    ## lowest : 5001 5005 5006 5007 5101, highest: 5521 5522 5523 5524 5525
    ## --------------------------------------------------------------------------------
    ## DC_TRT 
    ##        n  missing distinct 
    ##      102        0        2 
    ##                               
    ## Value        Control Treatment
    ## Frequency         51        51
    ## Proportion       0.5       0.5
    ## --------------------------------------------------------------------------------
    ## SEX 
    ##        n  missing distinct 
    ##      102        0        2 
    ##                     
    ## Value         F    M
    ## Frequency    53   49
    ## Proportion 0.52 0.48
    ## --------------------------------------------------------------------------------
    ## iq5 
    ##        n  missing distinct     Info     Mean      Gmd      .05      .10 
    ##       92       10       42    0.999    97.72    14.63    75.10    78.10 
    ##      .25      .50      .75      .90      .95 
    ##    90.75    98.00   106.00   114.00   117.45 
    ## 
    ## lowest :  71  74  76  77  78, highest: 118 119 121 123 125
    ## --------------------------------------------------------------------------------
    ## iq6 
    ##        n  missing distinct     Info     Mean      Gmd      .05      .10 
    ##       88       14       38    0.998    95.65    13.61    74.00    77.40 
    ##      .25      .50      .75      .90      .95 
    ##    88.75    97.00   103.25   108.30   113.65 
    ## 
    ## lowest :  70.0  73.0  74.0  76.0  78.0, highest: 114.0 118.0 119.0 126.0 126.5
    ## --------------------------------------------------------------------------------
    ## iq12 
    ##        n  missing distinct     Info     Mean      Gmd      .05      .10 
    ##       99        3       34    0.998    91.73    12.07     72.0     75.8 
    ##      .25      .50      .75      .90      .95 
    ##     85.0     92.0     99.0    104.2    111.1 
    ## 
    ## lowest :  71  72  74  75  76, highest: 108 111 112 114 115
    ## --------------------------------------------------------------------------------
    ## retn12 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      101        1        2    0.704       38   0.3762   0.4741 
    ## 
    ## --------------------------------------------------------------------------------
    ## iep12 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      100        2        2    0.691       36     0.36   0.4655 
    ## 
    ## --------------------------------------------------------------------------------
    ## iq15 
    ##        n  missing distinct     Info     Mean      Gmd      .05      .10 
    ##      102        0       30    0.998    93.18    11.98     76.1     80.0 
    ##      .25      .50      .75      .90      .95 
    ##     85.0     91.0    101.0    106.0    111.8 
    ## 
    ## lowest :  76  78  80  81  82, highest: 105 106 108 109 112
    ## --------------------------------------------------------------------------------
    ## hsgrad 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      102        0        2     0.74       57   0.5588    0.498 
    ## 
    ## --------------------------------------------------------------------------------
    ## parent 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      102        0        2    0.743       46    0.451   0.5001 
    ## 
    ## --------------------------------------------------------------------------------
    ## college 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      102        0        2     0.61       29   0.2843    0.411 
    ## 
    ## --------------------------------------------------------------------------------
    ## employed 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      101        1        2    0.738       57   0.5644   0.4966 
    ## 
    ## --------------------------------------------------------------------------------
    ## convicted 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      100        2        2     0.27       10      0.1   0.1818 
    ## 
    ## --------------------------------------------------------------------------------
    ## felon 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      100        2        2     0.27       10      0.1   0.1818 
    ## 
    ## --------------------------------------------------------------------------------
    ## jailed 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      101        1        2    0.439       18   0.1782   0.2958 
    ## 
    ## --------------------------------------------------------------------------------
    ## marijuana 
    ##        n  missing distinct     Info      Sum     Mean      Gmd 
    ##      102        0        2    0.701       64   0.6275   0.4721 
    ## 
    ## --------------------------------------------------------------------------------

### 5

``` r
#5
#I've used sapply to sum how many nulls i have in each column (it feels like to messy eith the describe)
na_count <-sapply(df_, function(y) sum(length(which(is.na(y)))))
print(na_count)
```

    ##   SUBJECT    DC_TRT       SEX       iq5       iq6      iq12    retn12     iep12 
    ##         0         0         0        10        14         3         1         2 
    ##      iq15    hsgrad    parent   college  employed convicted     felon    jailed 
    ##         0         0         0         0         1         2         2         1 
    ## marijuana 
    ##         0

``` r
df <- na.omit(df_)
na_count2 <-sapply(df, function(y) sum(length(which(is.na(y)))))
print(paste("Number of omitted observations: ", sum(na_count)-sum(na_count2)))
```

    ## [1] "Number of omitted observations:  36"

### 6

``` r
describe(df$SEX)
```

    ## df$SEX 
    ##        n  missing distinct 
    ##       82        0        2 
    ##                       
    ## Value          F     M
    ## Frequency     43    39
    ## Proportion 0.524 0.476

### 7-8

``` r
#7
#create a new dummy variable that mapping treated and sex.
df$treat <- ifelse(df$DC_TRT=="Control", 0, 1)
df$fem <- ifelse(df$SEX=="F", 1, 0)

#8
stargazer(subset(df,treat==0)[vars],type="html",title="Descriptive Statistics, Control group", digits=2,covariate.labels=vars)
```

<table style="text-align:center">
<caption>
<strong>Descriptive Statistics, Control group</strong>
</caption>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
Statistic
</td>
<td>
N
</td>
<td>
Mean
</td>
<td>
St. Dev.
</td>
<td>
Min
</td>
<td>
Pctl(25)
</td>
<td>
Pctl(75)
</td>
<td>
Max
</td>
</tr>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
iq5
</td>
<td>
41
</td>
<td>
94.17
</td>
<td>
13.75
</td>
<td>
71
</td>
<td>
84
</td>
<td>
103
</td>
<td>
123
</td>
</tr>
<tr>
<td style="text-align:left">
iq6
</td>
<td>
41
</td>
<td>
92.22
</td>
<td>
12.02
</td>
<td>
70
</td>
<td>
84
</td>
<td>
100
</td>
<td>
119
</td>
</tr>
<tr>
<td style="text-align:left">
iq12
</td>
<td>
41
</td>
<td>
88.61
</td>
<td>
11.47
</td>
<td>
71
</td>
<td>
82
</td>
<td>
94
</td>
<td>
115
</td>
</tr>
<tr>
<td style="text-align:left">
retn12
</td>
<td>
41
</td>
<td>
0.54
</td>
<td>
0.50
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
iep12
</td>
<td>
41
</td>
<td>
0.41
</td>
<td>
0.50
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
iq15
</td>
<td>
41
</td>
<td>
91.17
</td>
<td>
10.95
</td>
<td>
76
</td>
<td>
84
</td>
<td>
99
</td>
<td>
112
</td>
</tr>
<tr>
<td style="text-align:left">
hsgrad
</td>
<td>
41
</td>
<td>
0.51
</td>
<td>
0.51
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
parent
</td>
<td>
41
</td>
<td>
0.54
</td>
<td>
0.50
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
college
</td>
<td>
41
</td>
<td>
0.17
</td>
<td>
0.38
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
employed
</td>
<td>
41
</td>
<td>
0.49
</td>
<td>
0.51
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
convicted
</td>
<td>
41
</td>
<td>
0.10
</td>
<td>
0.30
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
felon
</td>
<td>
41
</td>
<td>
0.10
</td>
<td>
0.30
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
jailed
</td>
<td>
41
</td>
<td>
0.22
</td>
<td>
0.42
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
marijuana
</td>
<td>
41
</td>
<td>
0.61
</td>
<td>
0.49
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

``` r
stargazer(subset(df,treat==1)[vars],type="html",title="Descriptive Statistics, Treated group", digits=2,covariate.labels=vars)
```

<table style="text-align:center">
<caption>
<strong>Descriptive Statistics, Treated group</strong>
</caption>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
Statistic
</td>
<td>
N
</td>
<td>
Mean
</td>
<td>
St. Dev.
</td>
<td>
Min
</td>
<td>
Pctl(25)
</td>
<td>
Pctl(75)
</td>
<td>
Max
</td>
</tr>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
iq5
</td>
<td>
41
</td>
<td>
101.49
</td>
<td>
11.50
</td>
<td>
71
</td>
<td>
95
</td>
<td>
108
</td>
<td>
125
</td>
</tr>
<tr>
<td style="text-align:left">
iq6
</td>
<td>
41
</td>
<td>
99.09
</td>
<td>
12.10
</td>
<td>
70
</td>
<td>
92
</td>
<td>
105
</td>
<td>
126
</td>
</tr>
<tr>
<td style="text-align:left">
iq12
</td>
<td>
41
</td>
<td>
93.90
</td>
<td>
9.46
</td>
<td>
72
</td>
<td>
91
</td>
<td>
100
</td>
<td>
115
</td>
</tr>
<tr>
<td style="text-align:left">
retn12
</td>
<td>
41
</td>
<td>
0.24
</td>
<td>
0.43
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
iep12
</td>
<td>
41
</td>
<td>
0.29
</td>
<td>
0.46
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
iq15
</td>
<td>
41
</td>
<td>
94.88
</td>
<td>
9.96
</td>
<td>
76
</td>
<td>
87
</td>
<td>
103
</td>
<td>
112
</td>
</tr>
<tr>
<td style="text-align:left">
hsgrad
</td>
<td>
41
</td>
<td>
0.61
</td>
<td>
0.49
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
parent
</td>
<td>
41
</td>
<td>
0.39
</td>
<td>
0.49
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
college
</td>
<td>
41
</td>
<td>
0.37
</td>
<td>
0.49
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
employed
</td>
<td>
41
</td>
<td>
0.66
</td>
<td>
0.48
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
convicted
</td>
<td>
41
</td>
<td>
0.07
</td>
<td>
0.26
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
felon
</td>
<td>
41
</td>
<td>
0.07
</td>
<td>
0.26
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
jailed
</td>
<td>
41
</td>
<td>
0.15
</td>
<td>
0.36
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
</tr>
<tr>
<td style="text-align:left">
marijuana
</td>
<td>
41
</td>
<td>
0.61
</td>
<td>
0.49
</td>
<td>
0
</td>
<td>
0
</td>
<td>
1
</td>
<td>
1
</td>
</tr>
<tr>
<td colspan="8" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

## Part 2. Estimates of Average Treatment Effects

#### Denote that the sign of the effect is changing between variables (we want less from jailed but more IQ)

### 1

``` r
#subsetting only the treated and not treated mean of vars
mean_treated = sapply(subset(df, df$treat==1)[c(vars)], mean)
mean_not_treated = sapply(subset(df, df$treat==0)[c(vars)], mean)

#making the dataframe
means_table = data.frame(mean_treated, mean_not_treated, effect=mean_treated-mean_not_treated)

stargazer(means_table, type="html",title="Estimated average treatment effects", digits=2,summary = FALSE, covariate.labels = c("Var","Mean treated", "Mean not treated", "Avrage treatment effect"))
```

<table style="text-align:center">
<caption>
<strong>Estimated average treatment effects</strong>
</caption>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
Var
</td>
<td>
Mean treated
</td>
<td>
Mean not treated
</td>
<td>
Avrage treatment effect
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
iq5
</td>
<td>
101.49
</td>
<td>
94.17
</td>
<td>
7.32
</td>
</tr>
<tr>
<td style="text-align:left">
iq6
</td>
<td>
99.09
</td>
<td>
92.22
</td>
<td>
6.87
</td>
</tr>
<tr>
<td style="text-align:left">
iq12
</td>
<td>
93.90
</td>
<td>
88.61
</td>
<td>
5.29
</td>
</tr>
<tr>
<td style="text-align:left">
retn12
</td>
<td>
0.24
</td>
<td>
0.54
</td>
<td>
-0.29
</td>
</tr>
<tr>
<td style="text-align:left">
iep12
</td>
<td>
0.29
</td>
<td>
0.41
</td>
<td>
-0.12
</td>
</tr>
<tr>
<td style="text-align:left">
iq15
</td>
<td>
94.88
</td>
<td>
91.17
</td>
<td>
3.71
</td>
</tr>
<tr>
<td style="text-align:left">
hsgrad
</td>
<td>
0.61
</td>
<td>
0.51
</td>
<td>
0.10
</td>
</tr>
<tr>
<td style="text-align:left">
parent
</td>
<td>
0.39
</td>
<td>
0.54
</td>
<td>
-0.15
</td>
</tr>
<tr>
<td style="text-align:left">
college
</td>
<td>
0.37
</td>
<td>
0.17
</td>
<td>
0.20
</td>
</tr>
<tr>
<td style="text-align:left">
employed
</td>
<td>
0.66
</td>
<td>
0.49
</td>
<td>
0.17
</td>
</tr>
<tr>
<td style="text-align:left">
convicted
</td>
<td>
0.07
</td>
<td>
0.10
</td>
<td>
-0.02
</td>
</tr>
<tr>
<td style="text-align:left">
felon
</td>
<td>
0.07
</td>
<td>
0.10
</td>
<td>
-0.02
</td>
</tr>
<tr>
<td style="text-align:left">
jailed
</td>
<td>
0.15
</td>
<td>
0.22
</td>
<td>
-0.07
</td>
</tr>
<tr>
<td style="text-align:left">
marijuana
</td>
<td>
0.61
</td>
<td>
0.61
</td>
<td>
0
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

### 2

``` r
#getting only treated and not treated boys mean of vars 
mean_treated_M = sapply(subset(df, df$treat==1 & df$fem==0)[c(vars)], mean)
mean_not_treated_M = sapply(subset(df, df$treat==0 & df$fem==0)[c(vars)], mean)

means_table_M = data.frame(mean_treated_M, mean_not_treated_M, effect=mean_treated_M-mean_not_treated_M)

stargzer_boys <-  stargazer(means_table_M, type="html",title="Estimated average treatment effects for boys", digits=2,summary = FALSE, covariate.labels = c("Var","Mean treated", "Mean not treated", "Avrage treatment effect"))
```

<table style="text-align:center">
<caption>
<strong>Estimated average treatment effects for boys</strong>
</caption>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
Var
</td>
<td>
Mean treated
</td>
<td>
Mean not treated
</td>
<td>
Avrage treatment effect
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
iq5
</td>
<td>
100.75
</td>
<td>
91.16
</td>
<td>
9.59
</td>
</tr>
<tr>
<td style="text-align:left">
iq6
</td>
<td>
99.88
</td>
<td>
92
</td>
<td>
7.88
</td>
</tr>
<tr>
<td style="text-align:left">
iq12
</td>
<td>
93.25
</td>
<td>
90.47
</td>
<td>
2.78
</td>
</tr>
<tr>
<td style="text-align:left">
retn12
</td>
<td>
0.30
</td>
<td>
0.53
</td>
<td>
-0.23
</td>
</tr>
<tr>
<td style="text-align:left">
iep12
</td>
<td>
0.30
</td>
<td>
0.58
</td>
<td>
-0.28
</td>
</tr>
<tr>
<td style="text-align:left">
iq15
</td>
<td>
97.10
</td>
<td>
92.84
</td>
<td>
4.26
</td>
</tr>
<tr>
<td style="text-align:left">
hsgrad
</td>
<td>
0.50
</td>
<td>
0.58
</td>
<td>
-0.08
</td>
</tr>
<tr>
<td style="text-align:left">
parent
</td>
<td>
0.35
</td>
<td>
0.47
</td>
<td>
-0.12
</td>
</tr>
<tr>
<td style="text-align:left">
college
</td>
<td>
0.35
</td>
<td>
0.26
</td>
<td>
0.09
</td>
</tr>
<tr>
<td style="text-align:left">
employed
</td>
<td>
0.65
</td>
<td>
0.47
</td>
<td>
0.18
</td>
</tr>
<tr>
<td style="text-align:left">
convicted
</td>
<td>
0.15
</td>
<td>
0.21
</td>
<td>
-0.06
</td>
</tr>
<tr>
<td style="text-align:left">
felon
</td>
<td>
0.15
</td>
<td>
0.21
</td>
<td>
-0.06
</td>
</tr>
<tr>
<td style="text-align:left">
jailed
</td>
<td>
0.25
</td>
<td>
0.37
</td>
<td>
-0.12
</td>
</tr>
<tr>
<td style="text-align:left">
marijuana
</td>
<td>
0.80
</td>
<td>
0.68
</td>
<td>
0.12
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

``` r
#getting only treated and not treated females mean of vars 
mean_treated_F = sapply(subset(df, df$treat==1 & df$fem==1)[c(vars)], mean)
mean_not_treated_F = sapply(subset(df, df$treat==0 & df$fem==1)[c(vars)], mean)

means_table_F = data.frame(mean_treated_F, mean_not_treated_F, effect=mean_treated_F-mean_not_treated_F)

stargazer_girls <- stargazer(means_table_F, type="html",title="Estimated average treatment effects for girls", digits=2,summary = FALSE, covariate.labels = c("Var","Mean treated", "Mean not treated", "Avrage treatment effect"))
```

<table style="text-align:center">
<caption>
<strong>Estimated average treatment effects for girls</strong>
</caption>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
Var
</td>
<td>
Mean treated
</td>
<td>
Mean not treated
</td>
<td>
Avrage treatment effect
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
iq5
</td>
<td>
102.19
</td>
<td>
96.77
</td>
<td>
5.42
</td>
</tr>
<tr>
<td style="text-align:left">
iq6
</td>
<td>
98.33
</td>
<td>
92.41
</td>
<td>
5.92
</td>
</tr>
<tr>
<td style="text-align:left">
iq12
</td>
<td>
94.52
</td>
<td>
87
</td>
<td>
7.52
</td>
</tr>
<tr>
<td style="text-align:left">
retn12
</td>
<td>
0.19
</td>
<td>
0.55
</td>
<td>
-0.35
</td>
</tr>
<tr>
<td style="text-align:left">
iep12
</td>
<td>
0.29
</td>
<td>
0.27
</td>
<td>
0.01
</td>
</tr>
<tr>
<td style="text-align:left">
iq15
</td>
<td>
92.76
</td>
<td>
89.73
</td>
<td>
3.03
</td>
</tr>
<tr>
<td style="text-align:left">
hsgrad
</td>
<td>
0.71
</td>
<td>
0.45
</td>
<td>
0.26
</td>
</tr>
<tr>
<td style="text-align:left">
parent
</td>
<td>
0.43
</td>
<td>
0.59
</td>
<td>
-0.16
</td>
</tr>
<tr>
<td style="text-align:left">
college
</td>
<td>
0.38
</td>
<td>
0.09
</td>
<td>
0.29
</td>
</tr>
<tr>
<td style="text-align:left">
employed
</td>
<td>
0.67
</td>
<td>
0.50
</td>
<td>
0.17
</td>
</tr>
<tr>
<td style="text-align:left">
convicted
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
</tr>
<tr>
<td style="text-align:left">
felon
</td>
<td>
0
</td>
<td>
0
</td>
<td>
0
</td>
</tr>
<tr>
<td style="text-align:left">
jailed
</td>
<td>
0.05
</td>
<td>
0.09
</td>
<td>
-0.04
</td>
</tr>
<tr>
<td style="text-align:left">
marijuana
</td>
<td>
0.43
</td>
<td>
0.55
</td>
<td>
-0.12
</td>
</tr>
<tr>
<td colspan="4" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

## Part 3. Randomization Tests

### 1

``` r
#the answer to everything
set.seed(42)

#making the permutations of the treat variable (should be 82choose41, but
# the genperms is making it smaller by approximation)
permutations <- genperms(df$treat, blockvar=NULL, clustvar=NULL)
```

    ## Too many permutations to use exact method.
    ## Defaulting to approximate method.
    ## Increase maxiter to at least 4.24784580848791e+23 to perform exact estimation.

``` r
#making the probabilty of treatment under randomization
probs <- genprobexact(df$treat, clustvar = df$SUBJECT)

unique(probs)
```

    ## [1] 0.5

``` r
dim(permutations)
```

    ## [1]    82 10000

### 2

``` r
EstFunc <- function(df, perms, prob, vars) {
  #first create Ates that will produce a function that take each column,
  #and making estimated average treatment effects (ATEs).
  Ates <- function(vec) {
    PotentialOutcome <- genouts(vec, df$treat)
    ATES <- gendist(PotentialOutcome, perms, pr=unique(prob))
    return(ATES)
  }
  #then apply to all the vars
  ans <- sapply(df[c(vars)], Ates)
  return(ans)
}
```

### 3-4

``` r
PvalueTable <- function(df, perm, probs, means_table, vars) {
  

  #the estimates matrix (we want the absulute values)
  estimate <- abs(EstFunc(df,perm,probs,vars))
  
  #the actual means diffrence between the treat and control
  actual_diff <- abs(t(means_table["effect"]))
  
  #now we want to check how many values are higher (because its absulute we dont     care about the lower)
  raw_data <- c()
  for (col in colnames(actual_diff)) {
    counter <- sum(estimate[,c(col)] >= actual_diff[,c(col)])/10000
    raw_data <- c(raw_data, counter)
  }
  p_value_data <- data.frame(parameter = colnames(actual_diff), p_val =raw_data)
  #order the p_values
  p_value_data <- p_value_data[order(p_value_data$p_val),]
  #the total p_value
  p_value_data <- rbind(p_value_data, c("Total", round(mean(p_value_data$p_val), 4)))
  #making the numbers precentages
  p_value_data$p_val <- percent(as.numeric(p_value_data$p_val))
  return(p_value_data)
}

P_val_Total <- PvalueTable(df, permutations, probs, means_table, vars)
stargazer(t(P_val_Total),type="html",flip=T,title="P-Values for all 14 variables")
```

<table style="text-align:center">
<caption>
<strong>P-Values for all 14 variables</strong>
</caption>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
</td>
<td>
parameter
</td>
<td>
p\_val
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
1
</td>
<td>
iq5
</td>
<td>
1.170%
</td>
</tr>
<tr>
<td style="text-align:left">
4
</td>
<td>
retn12
</td>
<td>
1.170%
</td>
</tr>
<tr>
<td style="text-align:left">
2
</td>
<td>
iq6
</td>
<td>
1.240%
</td>
</tr>
<tr>
<td style="text-align:left">
3
</td>
<td>
iq12
</td>
<td>
2.440%
</td>
</tr>
<tr>
<td style="text-align:left">
9
</td>
<td>
college
</td>
<td>
8.200%
</td>
</tr>
<tr>
<td style="text-align:left">
6
</td>
<td>
iq15
</td>
<td>
11.980%
</td>
</tr>
<tr>
<td style="text-align:left">
10
</td>
<td>
employed
</td>
<td>
18.320%
</td>
</tr>
<tr>
<td style="text-align:left">
8
</td>
<td>
parent
</td>
<td>
27.320%
</td>
</tr>
<tr>
<td style="text-align:left">
5
</td>
<td>
iep12
</td>
<td>
36.340%
</td>
</tr>
<tr>
<td style="text-align:left">
7
</td>
<td>
hsgrad
</td>
<td>
50.930%
</td>
</tr>
<tr>
<td style="text-align:left">
13
</td>
<td>
jailed
</td>
<td>
55.810%
</td>
</tr>
<tr>
<td style="text-align:left">
11
</td>
<td>
convicted
</td>
<td>
100.000%
</td>
</tr>
<tr>
<td style="text-align:left">
12
</td>
<td>
felon
</td>
<td>
100.000%
</td>
</tr>
<tr>
<td style="text-align:left">
14
</td>
<td>
marijuana
</td>
<td>
100.000%
</td>
</tr>
<tr>
<td style="text-align:left">
15
</td>
<td>
Total
</td>
<td>
36.780%
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

### 5

We can see that in total 37% of the treatment effects in the
distribution that we created were at least as extreme (either as
vigorous or negative).

It means that in total we cannot reject the null hypothesis with good
level of confidence (5% or 10%).

But if we will look at the parameters separately, we can see that IQ and
college parameters are actually less than 10-5% so we can inference that
the treatment actually do make this parameters better - children will go
more to school and have a better IQ as we will go down the table below,
the P-value is getting higher and therefor a low significant to reject
the null hypothesis (like the employed, parent, iep12, hsgrad and jailed
parameter).

    ## 
    ## Estimated average treatment effects
    ## ===============================================================
    ## Var       Mean treated Mean not treated Avrage treatment effect
    ## ---------------------------------------------------------------
    ## iq5          101.49         94.17                7.32          
    ## iq6          99.09          92.22                6.87          
    ## iq12         93.90          88.61                5.29          
    ## retn12        0.24           0.54                -0.29         
    ## iep12         0.29           0.41                -0.12         
    ## iq15         94.88          91.17                3.71          
    ## hsgrad        0.61           0.51                0.10          
    ## parent        0.39           0.54                -0.15         
    ## college       0.37           0.17                0.20          
    ## employed      0.66           0.49                0.17          
    ## convicted     0.07           0.10                -0.02         
    ## felon         0.07           0.10                -0.02         
    ## jailed        0.15           0.22                -0.07         
    ## marijuana     0.61           0.61                  0           
    ## ---------------------------------------------------------------

As for the parameters that got 100%, lets remember that in our table
with the difference in means in section 2, we saw that this
parameters(convicted, felon, jailed, marijuana), have a minimal
difference between the treated and not treated.

So we can say that the treatment is effective but not in all the
parameters.

### 6

``` r
#Ive just took the process above and automate it with functions
PermProbs <- function(table) {
  permutations <- genperms(table$treat, blockvar=NULL, clustvar=NULL)
  probs <- genprobexact(table$treat, clustvar = table$SUBJECT)
  return(list(permutations, probs))
}

dfMale <- subset(df, df$fem==0)
dfFemale <- subset(df, df$fem==1)

M_perms_probs <- PermProbs(dfMale)
```

Too many permutations to use exact method. Defaulting to approximate
method. Increase maxiter to at least 68923264410 to perform exact
estimation.

``` r
F_perms_probs <- PermProbs(dfFemale)
```

Too many permutations to use exact method. Defaulting to approximate
method. Increase maxiter to at least 1052049481860 to perform exact
estimation.

``` r
P_val_Males <- PvalueTable(dfMale, M_perms_probs[[1]], M_perms_probs[[2]], means_table_M, vars)
P_val_Females <- PvalueTable(dfFemale, F_perms_probs[[1]], F_perms_probs[[2]], means_table_F, vars)

stargazer(t(P_val_Males),type="html",flip=T,title="P-Values for all 14 variables-Only Males")
```

<table style="text-align:center">
<caption>
<strong>P-Values for all 14 variables-Only Males</strong>
</caption>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
</td>
<td>
parameter
</td>
<td>
p\_val
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
1
</td>
<td>
iq5
</td>
<td>
2.9%
</td>
</tr>
<tr>
<td style="text-align:left">
5
</td>
<td>
iep12
</td>
<td>
5.2%
</td>
</tr>
<tr>
<td style="text-align:left">
2
</td>
<td>
iq6
</td>
<td>
7.0%
</td>
</tr>
<tr>
<td style="text-align:left">
4
</td>
<td>
retn12
</td>
<td>
10.6%
</td>
</tr>
<tr>
<td style="text-align:left">
10
</td>
<td>
employed
</td>
<td>
20.4%
</td>
</tr>
<tr>
<td style="text-align:left">
6
</td>
<td>
iq15
</td>
<td>
21.7%
</td>
</tr>
<tr>
<td style="text-align:left">
14
</td>
<td>
marijuana
</td>
<td>
27.4%
</td>
</tr>
<tr>
<td style="text-align:left">
3
</td>
<td>
iq12
</td>
<td>
45.0%
</td>
</tr>
<tr>
<td style="text-align:left">
13
</td>
<td>
jailed
</td>
<td>
50.0%
</td>
</tr>
<tr>
<td style="text-align:left">
8
</td>
<td>
parent
</td>
<td>
52.2%
</td>
</tr>
<tr>
<td style="text-align:left">
11
</td>
<td>
convicted
</td>
<td>
68.8%
</td>
</tr>
<tr>
<td style="text-align:left">
12
</td>
<td>
felon
</td>
<td>
68.8%
</td>
</tr>
<tr>
<td style="text-align:left">
9
</td>
<td>
college
</td>
<td>
73.4%
</td>
</tr>
<tr>
<td style="text-align:left">
7
</td>
<td>
hsgrad
</td>
<td>
75.3%
</td>
</tr>
<tr>
<td style="text-align:left">
15
</td>
<td>
Total
</td>
<td>
37.8%
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

``` r
stargazer(t(P_val_Females),type="html",flip=T,title="P-Values for all 14 variables-Only Females")
```

<table style="text-align:center">
<caption>
<strong>P-Values for all 14 variables-Only Females</strong>
</caption>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
</td>
<td>
parameter
</td>
<td>
p\_val
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
<tr>
<td style="text-align:left">
3
</td>
<td>
iq12
</td>
<td>
1.80%
</td>
</tr>
<tr>
<td style="text-align:left">
4
</td>
<td>
retn12
</td>
<td>
2.53%
</td>
</tr>
<tr>
<td style="text-align:left">
9
</td>
<td>
college
</td>
<td>
3.10%
</td>
</tr>
<tr>
<td style="text-align:left">
7
</td>
<td>
hsgrad
</td>
<td>
6.83%
</td>
</tr>
<tr>
<td style="text-align:left">
2
</td>
<td>
iq6
</td>
<td>
10.34%
</td>
</tr>
<tr>
<td style="text-align:left">
1
</td>
<td>
iq5
</td>
<td>
16.96%
</td>
</tr>
<tr>
<td style="text-align:left">
6
</td>
<td>
iq15
</td>
<td>
33.73%
</td>
</tr>
<tr>
<td style="text-align:left">
10
</td>
<td>
employed
</td>
<td>
35.46%
</td>
</tr>
<tr>
<td style="text-align:left">
8
</td>
<td>
parent
</td>
<td>
37.29%
</td>
</tr>
<tr>
<td style="text-align:left">
14
</td>
<td>
marijuana
</td>
<td>
54.36%
</td>
</tr>
<tr>
<td style="text-align:left">
5
</td>
<td>
iep12
</td>
<td>
73.55%
</td>
</tr>
<tr>
<td style="text-align:left">
11
</td>
<td>
convicted
</td>
<td>
100.00%
</td>
</tr>
<tr>
<td style="text-align:left">
12
</td>
<td>
felon
</td>
<td>
100.00%
</td>
</tr>
<tr>
<td style="text-align:left">
13
</td>
<td>
jailed
</td>
<td>
100.00%
</td>
</tr>
<tr>
<td style="text-align:left">
15
</td>
<td>
Total
</td>
<td>
41.14%
</td>
</tr>
<tr>
<td colspan="3" style="border-bottom: 1px solid black">
</td>
</tr>
</table>

Lets try to see what are the differences with boys and girls:

It seems like the IQ at younger ages (5, 6) is improved better at boys
then girls, but the IQ of girls at older age (15) improved more then
boys.

We can see that the employment is impacted more at the boys group then
the girls (19.8%&lt;36.28%).

it looks like the males are more effected by the “crime” parameters than
girls (probably because girls from the first place are less involved in
crime).

In conclusion there is a slight difference between boys and girls in
some variables.
