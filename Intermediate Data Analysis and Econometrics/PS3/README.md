PS3
================
Yonatan-Lourie
5/6/2021

``` r
library(tidyverse)
library(sandwich)
library(lmtest)
library(car)
```

# Problem Set 3

## Part 1. Research Question & Data

### 1.

#### (a) TODO

The creators of BTB believed the policy would reduce racial differences
in employment because they thought that if some employer will not get
information about the employee criminal history, they will be less tend
to reject his applicant. If so, they believed that the policy will
reduce discrimination by race in the labor market.

#### (b) TODO

-   One hypothesis is that employers (with lack of data about criminal
    record) will conclude that because someone is black - he is more
    likely to have a criminal record (although he does not have). The
    employer will conclude it by racism assumptions.
-   The second hypothesis is the employers may care alot about the
    criminal records of their applicants and will find about the
    applicant criminal record in any case. \#\#\# 2.

``` r
df <- read.table("AganStarrQJEData.csv", sep = ",", header = TRUE)
df <- subset(df, remover != -1)
head(df)
```

    ##   X nj nyc app_date_d pre post storeid chain_id center crimbox crime drugcrime
    ## 1 1  1   0 2015-02-14   1    0       1        2    114       0     1         0
    ## 2 2  1   0 2015-05-20   0    1       1        2    114       0     0         0
    ## 3 3  1   0 2015-02-08   1    0       1        2    114       0     0         0
    ## 4 4  1   0 2015-05-11   0    1       1        2    114       0     1         1
    ## 5 5  1   0 2015-06-03   0    1       3        3    106       0     0         0
    ## 6 6  1   0 2015-06-02   0    1       3        3    106       0     1         0
    ##   propertycrime ged empgap white black remover noncomplier_store balanced
    ## 1             1   1      1     0     1       0                 0        1
    ## 2             0   1      1     1     0       0                 0        1
    ## 3             0   1      0     1     0       0                 0        1
    ## 4             0   1      0     0     1       0                 0        1
    ## 5             0   1      1     0     1       0                 0        0
    ## 6             1   0      0     1     0       0                 0        0
    ##   response response_date_d daystoresponse interview cogroup_comb cogroup_njnyc
    ## 1        0              NA             NA         0        90037           157
    ## 2        0              NA             NA         0        90037           157
    ## 3        0              NA             NA         0        90037           157
    ## 4        0              NA             NA         0        90037           157
    ## 5        0              NA             NA         0            3             1
    ## 6        0              NA             NA         0            3             1
    ##   post_cogroup_njnyc white_cogroup_njnyc ged_cogroup_njnyc empgap_cogroup_njnyc
    ## 1                  0                   0               157                  157
    ## 2                157                 157               157                  157
    ## 3                  0                 157               157                    0
    ## 4                157                   0               157                    0
    ## 5                  1                   0                 1                    1
    ## 6                  1                   1                 0                    0
    ##   box_white pre_white post_white white_nj post_remover_ged post_ged remover_ged
    ## 1         0         0          0        0                0        0           0
    ## 2         0         0          1        1                0        1           0
    ## 3         0         1          0        1                0        0           0
    ## 4         0         0          0        0                0        1           0
    ## 5         0         0          0        0                0        1           0
    ## 6         0         0          1        1                0        0           0
    ##   post_remover_empgap post_empgap remover_empgap post_remover_white
    ## 1                   0           0              0                  0
    ## 2                   0           1              0                  0
    ## 3                   0           0              0                  0
    ## 4                   0           0              0                  0
    ## 5                   0           1              0                  0
    ## 6                   0           0              0                  0
    ##   post_remover remover_white raerror retail num_stores avg_salesvolume
    ## 1            0             0       0      0          7        1969.286
    ## 2            0             0       0      0          7        1969.286
    ## 3            0             0       0      0          7        1969.286
    ## 4            0             0       0      0          7        1969.286
    ## 5            0             0       0      1        495        1937.000
    ## 6            0             0       0      1        495        1937.000
    ##   avg_num_employees retail_white retail_post retail_post_white  percblack
    ## 1         40.857143            0           0                 0 0.03703704
    ## 2         40.857143            0           0                 0 0.03703704
    ## 3         40.857143            0           0                 0 0.03703704
    ## 4         40.857143            0           0                 0 0.03703704
    ## 5          7.064646            0           1                 0 0.44549125
    ## 6          7.064646            1           1                 1 0.44549125
    ##   percwhite tot_crime_rate nocrimbox nocrime_box nocrime_pre response_white
    ## 1 0.9056438       7.176834         1           0           0             NA
    ## 2 0.9056438       7.176834         1           0           0              0
    ## 3 0.9056438       7.176834         1           0           1              0
    ## 4 0.9056438       7.176834         1           0           0             NA
    ## 5 0.2476447      69.903038         1           0           0             NA
    ## 6 0.2476447      69.903038         1           0           0              0
    ##   response_black response_ged response_hsd response_empgap response_noempgap
    ## 1              0            0           NA               0                NA
    ## 2             NA            0           NA               0                NA
    ## 3             NA            0           NA              NA                 0
    ## 4              0            0           NA              NA                 0
    ## 5              0            0           NA               0                NA
    ## 6             NA           NA            0              NA                 0

## Part 2. Review of Law of Iterated Expectations

### 1

We wish to calculate:
*E*(*X*) = *P*(*W*)*P*(*r**e**s**p**o**n**s**e*\|*W**h**i**t**e*) + *P*(*B*)*P*(*r**e**s**p**o**n**s**e*\|*B**l**a**c**k*)
were X=callback rate.

``` r
df$responseW <- ifelse(df$white==1, df$response, NA)
df$responseB <- ifelse(df$white==0, df$response, NA)

P_r_w <- mean(df$responseW, na.rm = TRUE)
P_r_b <- mean(df$responseB, na.rm = TRUE)

P_w <- mean(df$white, na.rm = TRUE)
P_b <- mean(df$black, na.rm = TRUE)

LIE_response <- P_r_w*P_w + P_r_b*P_b 
responseRate <- mean(df$response)

LIE_response
```

    ## [1] 0.1171688

``` r
responseRate
```

    ## [1] 0.1171688

## Part 3. Equivalence between Linear Regression with Fully Saturated Model and Conditional Means

### 1.

For
*E*(*Y*\|*X*<sub>1</sub>, *X*<sub>2</sub>) = *β*<sub>0</sub> + *β*<sub>1</sub>*X*<sub>1</sub> + *β*<sub>2</sub>*X*<sub>2</sub> + *β*<sub>3</sub>*X*<sub>1</sub>*X*<sub>2</sub>
the conditional means for *β*<sub>*i*</sub> are:

*β*<sub>1</sub> = *E*(*Y*<sub>*i*</sub>\|*X*<sub>1*i*</sub> = 1, *X*<sub>2*i*</sub> = 0)

*β*<sub>2</sub> = *E*(*Y*<sub>*i*</sub>\|*X*<sub>1*i*</sub> = 0, *X*<sub>2*i*</sub> = 1)

*β*<sub>3</sub> = *E*(*Y*<sub>*i*</sub>\|*X*<sub>1*i*</sub> = 1, *X*<sub>2*i*</sub> = 1)

### 2.

``` r
df_ex3b <- subset(df, post==0 & remover==0)
ex3b.model <- lm(response ~white,data=df_ex3b)
summary(ex3b.model)
```

    ## 
    ## Call:
    ## lm(formula = response ~ white, data = df_ex3b)
    ## 
    ## Residuals:
    ##      Min       1Q   Median       3Q      Max 
    ## -0.12301 -0.12301 -0.09615 -0.09615  0.90385 
    ## 
    ## Coefficients:
    ##             Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept) 0.096154   0.006314  15.228  < 2e-16 ***
    ## white       0.026854   0.008927   3.008  0.00264 ** 
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1
    ## 
    ## Residual standard error: 0.3122 on 4889 degrees of freedom
    ## Multiple R-squared:  0.001848,   Adjusted R-squared:  0.001643 
    ## F-statistic: 9.049 on 1 and 4889 DF,  p-value: 0.002641

#### a.

The estimated *α*<sub>1</sub> is 0.0268539.

We can use *α*<sub>1</sub> as a consistent estimator of the effect of
race on callback probability without worrying about omitted variable
bias because the effect of race is depend only on a binary (dummy)
variable (where *W**h**i**t**e*<sub>*i*</sub> is 0 it means that
*B**l**a**c**k*<sub>*i*</sub> is 1).

We also can observe that the variance of *β*<sub>*w**h**i**t**e*</sub>
is really small, and we have alot of observations (14637 .

#### b

``` r
a_0 <- ex3b.model$coefficients[1]
mean_black_callback <- mean(na.omit(df_ex3b$responseB))

round(a_0,3) == round(mean_black_callback,3)
```

    ## (Intercept) 
    ##        TRUE

#### c

``` r
a_1 <- ex3b.model$coefficients[2]
mean_callback <- mean(na.omit(df_ex3b$responseW))
round(mean_callback,3) == round(a_1+a_0,3)
```

    ## white 
    ##  TRUE

### 3

*C**a**l**l**b**a**c**k*<sub>*i*</sub> = *λ*<sub>0</sub> + *λ*<sub>1</sub>*W**h**i**t**e*<sub>*i*</sub> + *λ*<sub>2</sub>*C**r**i**m**e*<sub>*i*</sub> + *λ*<sub>3</sub>(*W**h**i**t**e**X**C**r**i**m**e*<sub>*i*</sub>) + *ϵ*<sub>*i*</sub>

``` r
df_ex3c <- subset(df, remover==1 & pre==1)
ex3c.model <- lm(response ~white*crime,data=df_ex3c)
```

#### a.

In question 1, there is no employers with a box in the pre period, so
nothing will change except for the ratio of white people who will get
callback (which can be implicated by the rest of the employers). Because
those employers didnt have the box in the first place - they probably
care less about the criminal record.

In the other hand, here we have employers that had box and will remove
it in the post period, so we need the criminal record to observe the
difference that the box will do the callback rate.

#### b.

``` r
summary(ex3c.model)
```

    ## 
    ## Call:
    ## lm(formula = response ~ white * crime, data = df_ex3c)
    ## 
    ## Residuals:
    ##      Min       1Q   Median       3Q      Max 
    ## -0.13833 -0.12698 -0.08769 -0.08418  0.91582 
    ## 
    ## Coefficients:
    ##              Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)  0.126984   0.013070   9.716   <2e-16 ***
    ## white        0.011349   0.018227   0.623   0.5336    
    ## crime       -0.042809   0.018272  -2.343   0.0192 *  
    ## white:crime -0.007835   0.025664  -0.305   0.7602    
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1
    ## 
    ## Residual standard error: 0.3112 on 2350 degrees of freedom
    ## Multiple R-squared:  0.005831,   Adjusted R-squared:  0.004562 
    ## F-statistic: 4.594 on 3 and 2350 DF,  p-value: 0.003273

``` r
lambdas_3c <-  ex3c.model$coefficients
```

We can see that the estimator for the white variable is smaller here
(0.0268539 for pew period without box, and 0.0113492 for this pre period
with box). The reason is that *α*<sub>1</sub> is not taking in account
the box variable which can “identify” white people with a box.

*β*<sub>0</sub> here is higher than *α*<sub>0</sub>. The constant here
means the impact of being black without box, and for *α*<sub>0</sub> is
only for being black, so for black with a box we have a smaller callback
rate.

We can compare *β*<sub>2</sub> and *β*<sub>3</sub> and notice that for a
black man who did a crime have much more negative impact on the call
rate: -0.042809 vs -0.0078346. But the white crime impact is getting the
value of *β*<sub>2</sub> so its converge to a quiet small difference:
-0.042809 vs -0.0506436.

We can use *λ̂*<sub>1</sub> and *λ̂*<sub>1</sub> + *λ̂*<sub>3</sub> without
worrying about omitted variable bias, because there is no other omitted
variable that can effect those variables. *λ̂*<sub>1</sub> is a binary
variable - White or Black (only tow options), and for
*λ̂*<sub>1</sub> + *λ̂*<sub>3</sub> we have also only tow options because
if white =0, then *λ̂*<sub>1</sub> + *λ̂*<sub>3</sub> will be 0. \#\#\#\#
c

``` r
lambda_0_hat <- as.numeric(lambdas_3c[1])
round(mean(subset(df, pre==1 &remover==1 & crime==0 & black==1)$response),4) == round(lambda_0_hat,4)
```

    ## [1] TRUE

Indeed, *λ̂*<sub>0</sub> equals to the sample mean of callback among
black applicants without a criminal record (in the pre-period for
employers with box), which equal 0.1269841 .

#### d.

Recall:
*C**a**l**l**b**a**c**k*<sub>*i*</sub> = *λ*<sub>0</sub> + *λ*<sub>1</sub>*W**h**i**t**e*<sub>*i*</sub> + *λ*<sub>2</sub>*C**r**i**m**e*<sub>*i*</sub> + *λ*<sub>3</sub>(*W**h**i**t**e**X**C**r**i**m**e*<sub>*i*</sub>) + *ϵ*<sub>*i*</sub>

I will show that:

*λ*<sub>0</sub> = *E*(*C**a**l**l**b**a**c**k*<sub>*c**b*</sub>\|*W**h**i**t**e* = 0, *C**r**i**m**e* = 0)

*λ*<sub>1</sub> = *E*(*C**a**l**l**b**a**c**k*<sub>*c**b*</sub>\|*W**h**i**t**e* = 1, *C**r**i**m**e* = 0) − *E*(*C**a**l**l**b**a**c**k*<sub>*c**b*</sub>\|*W**h**i**t**e* = 0, *C**r**i**m**e* = 0)

*λ*<sub>2</sub> = *E*(*C**a**l**l**b**a**c**k*<sub>*c**b*</sub>\|*W**h**i**t**e* = 0, *C**r**i**m**e* = 1) − *E*(*C**a**l**l**b**a**c**k*<sub>*c**b*</sub>\|*W**h**i**t**e* = 0, *C**r**i**m**e* = 0)

$\\lambda\_3 = E(Callback\_{cb}\|White=1, Crime=1)- E(Callback\_{cb}\|White=1, Crime=0)\\\\- E(Callback\_{cb}\|White=0, Crime=1)- E(Callback\_{cb}\|White=0, Crime=0)$

``` r
df_3 <- subset(df, remover==1 &pre==1)
l_0 <-mean(subset(df_3, crime==0 & white==0)$response)

l_1 <- mean(subset(df_3, crime==0 & white==1)$response)-mean(subset(df_3, crime==0 & white==0)$response)

l_2 <- mean(subset(df_3, crime==1 & white==0)$response)-mean(subset(df_3, crime==0 & white==0)$response)

l_3 <- mean(subset(df_3, crime==1 & white==1)$response)-mean(subset(df_3, crime==0 & white==1)$response) - (mean(subset(df_3, crime==1 & white==0)$response)-mean(subset(df_3, crime==0 & white==0)$response))

results_3 <- cbind(data.frame(ex3c.model$coefficients),c(l_0,l_1,l_2,l_3))
colnames(results_3) <- c("Model coeff","Remainig sample means")
results_3
```

    ##              Model coeff Remainig sample means
    ## (Intercept)  0.126984127           0.126984127
    ## white        0.011349206           0.011349206
    ## crime       -0.042809043          -0.042809043
    ## white:crime -0.007834577          -0.007834577

And that:

$ E(Callback\|White=0, Crime=1) = \_0+\_2$ = 0.0841751 $
E(Callback\|White=1, Crime=0) = \_0+\_1$ = 0.1383333 $
E(Callback\|White=1, Crime=1) = \_0+\_2$ = 0.0876897

``` r
l_0l_2 <- mean(subset(df_3, white==0 & crime==1)$response)
l_0l_1 <- mean(subset(df_3, white==1 & crime==0)$response)
all_lambdas <- mean(subset(df_3, white==1 & crime==1)$response) 

res3d <- data.frame(OLSlambdas=c(l_0+l_2, l_0+l_1,l_0+l_1+l_2+l_3), ConditionalMeansLambdas = c(l_0l_2, l_0l_1, all_lambdas))
rownames(res3d) = c("Black Criminal", "White no Criminal", "White criminal")

res3d
```

    ##                   OLSlambdas ConditionalMeansLambdas
    ## Black Criminal    0.08417508              0.08417508
    ## White no Criminal 0.13833333              0.13833333
    ## White criminal    0.08768971              0.08768971

#### e.

By the law of iterated expectation we can use:
*E*(*W**h**i**t**e*) = *E*(*W**h**i**t**e*\|*C**r**i**m**e*) = *P*(*C**r**i**m**e* = 1) \* *E*(*W**h**i**t**e*\|*C**r**i**m**e* = 1) + *P*(*C**r**i**m**e* = 0) \* *E*(*W**h**i**t**e*\|*C**r**i**m**e* = 0)
We already found the corresponding lambdas in the last section
(*λ*<sub>0</sub> + *λ*<sub>1</sub> + *λ*<sub>2</sub> + *λ*<sub>3</sub>
for crime==1 and *λ*<sub>0</sub> + *λ*<sub>1</sub> for crime==0). And
for the calclations:

``` r
p_1 <- mean(df_3$crime)
(all_lambdas)*p_1 + (l_0+l_1)*(1-p_1)
```

    ## [1] 0.1127964

#### f.

after we have found the effect of being white in the previews question
we see that in the post period without a box the effect of being white
is way higher than the effect of being white with a box. that means that
if there is no box the effect of being white is more than double than
the effect of being whit if there is a box in the pre period!

another interesting results is that with the box your chances of getting
a call back are only a tiny bit different if you are white or if you are
black and you both have criminal records. where if you are white and you
are black and you both don’t have criminal records than being white adds
to your chances of getting a call back but way less than the situation
with no box.

so the results are basically saying there is a discrimination anyway but
having no box , even in the pre period , makes black people life harder
trying to get into a job.

The White coefficient *α*<sub>1</sub> from section 2 (without box) is
bigger than *α*<sub>1</sub> from section e (with box). Thus, when the
box was removed the positive impact for white callback was stronger from
those who didnt have box in the first place.

We can also observed that without box the different of getting a
callback between black and white is very big. Which means that the box
is indeed making a discrimination against blacks (The discrimination
exist also without a box but less significant).

### 4.

*C**a**l**l**b**a**c**k*<sub>*i*</sub> = *β*<sub>0</sub> + *β*<sub>1</sub>*W**h**i**t**e*<sub>*i*</sub> + *β*<sub>2</sub>*B**o**x*<sub>*i*</sub> + *β*<sub>3</sub>(*W**h**i**t**e*<sub>*i*</sub>*B**o**x*<sub>*i*</sub>) + *β*<sub>4</sub>(*C**r**i**m**e*<sub>*i*</sub>*B**o**x*<sub>*i*</sub>) + *β*<sub>5</sub>(*W**h**i**t**e*<sub>*i*</sub>*C**r**i**m**e*<sub>*i*</sub>*B**o**x*<sub>*i*</sub>)

#### a.

``` r
df_ex4 <- subset(df, pre==1)
ex4.model <- lm(data=df_ex4, response ~ white + remover + (white:remover) +(crime:remover)+(white:crime:remover))

summary(ex4.model)
```

    ## 
    ## Call:
    ## lm(formula = response ~ white + remover + (white:remover) + (crime:remover) + 
    ##     (white:crime:remover), data = df_ex4)
    ## 
    ## Residuals:
    ##      Min       1Q   Median       3Q      Max 
    ## -0.13833 -0.12301 -0.09615 -0.08769  0.91582 
    ## 
    ## Coefficients:
    ##                      Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)          0.096154   0.006308  15.243  < 2e-16 ***
    ## white                0.026854   0.008918   3.011  0.00261 ** 
    ## remover              0.030830   0.014536   2.121  0.03396 *  
    ## white:remover       -0.015505   0.020326  -0.763  0.44560    
    ## remover:crime       -0.042809   0.018309  -2.338  0.01941 *  
    ## white:remover:crime -0.007835   0.025716  -0.305  0.76064    
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1
    ## 
    ## Residual standard error: 0.3118 on 7239 degrees of freedom
    ## Multiple R-squared:  0.003139,   Adjusted R-squared:  0.002451 
    ## F-statistic: 4.559 on 5 and 7239 DF,  p-value: 0.0003742

``` r
betas_3 <- ex4.model$coefficients
```

*β*<sub>0</sub> = *E*(*C**a**l**l**b**a**c**k*\|*W* = 0, *B* = 0, *C* = 0)=
0.0961538 absorbing the error and the complement of the binary variables
(Black, no criminal, no box).

*β*<sub>1</sub> = *E*(*C**a**l**l**b**a**c**k*\|*W* = 1, *B* = 0, *C* = 0) − *E*(*C**a**l**l**b**a**c**k*\|*W* = 0, *B* = 0, *C* = 0)=
0.0268539 impact of white on the response.

*β*<sub>2</sub> = *E*(*C**a**l**l**b**a**c**k*\|*W* = 0, *B* = 1, *C* = 0) − *E*(*C**a**l**l**b**a**c**k*\|*W* = 0, *B* = 0, *C* = 0)=
0.0308303 impact of the box on the response.

$\\beta\_3 = E(Callback\|W=1,B=1,C=0) - \\sum\_{i = 0}^{2} \\beta\_i$ =
-0.042809 impact of the interaction of white and box.

*β*<sub>4</sub> = *E*(*C**a**l**l**b**a**c**k*\|*W* = 0, *B* = 1, *C* = 1) − *β*<sub>0</sub> − *β*<sub>2</sub> − *β*<sub>3</sub>=
-0.0078346 impact of the interaction of crime and box.

$\\beta\_5 = E(Callback\|W=1,B=1,C=1) - \\sum\_{i = 0}^{6} \\beta\_i =$
-0.0078346 impact of the interaction of crime, box and white.

#### b.

*β*<sub>0</sub> = *α*<sub>0</sub>

*β*<sub>1</sub> = *α*<sub>1</sub> because *α*<sub>1</sub> is just the
white impact without the box (because we have
*β*<sub>3</sub>(*W**h**i**t**e* \* *B**o**x*))
*β*<sub>2</sub> = *λ*<sub>0</sub> − *α*<sub>0</sub> because
*E*\[*Y*\|*b**o**x*, *b**l**a**c**k*, *n**o* − *c**r**i**m**e*\] − *E*\[*Y*\|*n**o* − *b**o**x*, *b**l**a**c**k*, *n**o* − *c**r**i**m**e*\]

*β*<sub>3</sub> = *λ*<sub>1</sub> − *α*<sub>1</sub> because the total
impact with the box (of betas) is *β*<sub>1</sub> + *β*<sub>3</sub> and
we know that *β*<sub>1</sub> = *α*<sub>1</sub>

*β*<sub>4</sub> = *λ*<sub>2</sub> because $(E\[Y\|black, box, crime\] -
E\[Y\|black, box, no-crime\]) - (E\[Y\|black, no-box,
crime\]-E\[Y\|black, no-box. no-crime\]) $ *β*<sub>5</sub>= TODO

Calculations made by conditional means and algebra.

#### c.

``` r
betas_3 <- round(betas_3, 3)
round(a_0 ,3) == betas_3[1] #beta_0
```

    ## (Intercept) 
    ##        TRUE

``` r
round(a_1 ,3) == betas_3[2] #beta_1
```

    ## white 
    ##  TRUE

``` r
round(l_0-a_0 ,3) == betas_3[3] #beta_2
```

    ## (Intercept) 
    ##        TRUE

``` r
round(l_1-a_1 ,3) == betas_3[4] #beta_3
```

    ## white 
    ##  TRUE

``` r
round(l_2 ,3) == betas_3[5] #beta_4
```

    ## remover:crime 
    ##          TRUE

#### d

Recall that *α*<sub>1</sub> is the effect of being white without a box,
and that *λ*<sub>1</sub> is the effect of being white with a box The
null hypothesis is that the impact of white people on the callback is
not changing in respect to the box (if they had or not box in the pre
period).

#### e

$E(callback\|White,box,no crime) = E(callback\|White,box,no crime) \\leftrightarrow \\alpha\_1=\\lambda\_1 \\\\ \\beta\_0+\\beta\_1+\\beta\_2+\\beta\_3 = \\beta\_0+\\beta\_1$

Which means that the null hypothesis for betas is
*β*<sub>2</sub> + *β*<sub>3</sub> = 0 \#\#\#\# f We dont know in from
which part of the city the empolyers are (NJ and NY). For example, if
most of the employers will be from a particular neighborhood in Bronx we
will have selection bias, because there some neighberhoods with more
black people there so the employers will have different racism view that
connected to the box.

### 5

``` r
ex5.model <- coeftest(ex4.model, df = Inf, vcov = vcovHC)
betas_5 <- coef(ex5.model)
```

#### a

We want to estimate
*E*(*Y*\|*W**h**i**t**e* = 1, *B**o**x* = 0, *C**r**i**m**e* = 1) − *E*(*Y*\|*W**h**i**t**e* = 0, *B**o**x* = 0, *C**r**i**m**e* = 1) = *β*<sub>0</sub> + *β*<sub>1</sub> − *β*<sub>0</sub> = *β*<sub>1</sub>

``` r
ex5.model
```

    ## 
    ## z test of coefficients:
    ## 
    ##                       Estimate Std. Error z value  Pr(>|z|)    
    ## (Intercept)          0.0961538  0.0059656 16.1179 < 2.2e-16 ***
    ## white                0.0268539  0.0089281  3.0078  0.002631 ** 
    ## remover              0.0308303  0.0152250  2.0250  0.042869 *  
    ## white:remover       -0.0155047  0.0218002 -0.7112  0.476948    
    ## remover:crime       -0.0428090  0.0180673 -2.3694  0.017816 *  
    ## white:remover:crime -0.0078346  0.0257122 -0.3047  0.760593    
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

##### i

The estimate is of *β*<sub>1</sub> is 0.0268539

##### ii

The std is 0.0089281

##### iii

``` r
test_stat <-ex5.model[2,c(1,2,3)][1]/ex5.model[2,c(1,2,3)][2]
test_stat > qt(0.95, dim(df)[1]-1)
```

    ## Estimate 
    ##     TRUE

We will reject the null hypothesis at 0.05 significance level \#\#\#\#\#
iv

``` r
coefci(ex4.model, df = dim(df)[1]-1, level=.95, vcov = vcovHC)[2,]
```

    ##       2.5 %      97.5 % 
    ## 0.009353782 0.044354055

##### v

At 95% confidence level, the estimator of the callback rate in the
pre-period for employers who don’t have a box in the pre period will be
between 0.009 and 0.04. The CI is not containing zero in it, so we
reject the hypothesis that the the effect of being white with no box is
0. We also saw in the last section that being white giving a positive
coefficients so we can conclude that being white is good to get
responses.

#### b

We want to estimate
*E*(*Y*\|*W**h**i**t**e* = 1, *B**o**x* = 1, *C**r**i**m**e* = 0)

``` r
df_ex5 <- subset(df, pre==1 &crime==0 &remover==1)
ex5b.model <- lm(data=df_ex5, response ~ white)

ex5b.model.robust <- coeftest(ex5b.model, df = Inf, vcov = vcovHC)
betas_5 <- coef(ex5b.model.robust)
ex5b.model.robust[,c(1,2,3,4)]
```

    ##               Estimate Std. Error   z value     Pr(>|z|)
    ## (Intercept) 0.12698413 0.01400751 9.0654330 1.241104e-19
    ## white       0.01134921 0.01988809 0.5706534 5.682346e-01

The estimate is 0.0113492

The std is 0.0198881

``` r
test_stat <- ex5b.model.robust[2,c(1,2,3,4)][3]
as.numeric(test_stat) > qt(0.95, dim(df_ex5)[1]-1)
```

    ## [1] FALSE

``` r
coefci(ex5b.model, df = dim(df)[1]-1, level=.95, vcov = vcovHC)[2,]
```

    ##       2.5 %      97.5 % 
    ## -0.02763396  0.05033237

Hence, we will not reject the null hypothesis.

#### c

And the same for
*E*(*Y*\|*W**h**i**t**e* = 1, *B**o**x* = 1, *C**r**i**m**e* = 1)

``` r
df_ex5 <- subset(df, pre==1 &crime==1 &remover==1)

ex5b.model <- lm(data=df_ex4, response ~ white)

ex5b.model.robust <- coeftest(ex5b.model, df = Inf, vcov = vcovHC)
betas_5 <- coef(ex5b.model.robust)
ex5b.model.robust[,c(1,2,3,4)]
```

    ##               Estimate  Std. Error   z value     Pr(>|z|)
    ## (Intercept) 0.09902913 0.004976279 19.900237 4.050103e-88
    ## white       0.02075109 0.007331054  2.830574 4.646453e-03

``` r
test_stat <- ex5b.model.robust[2,c(1,2,3,4)][3]
test_stat > qt(0.95, dim(df)[1]-1)
```

    ## z value 
    ##    TRUE

``` r
coefci(ex5b.model, df = dim(df_ex5)[1]-1, level=.95, vcov = vcovHC)[2,]
```

    ##       2.5 %      97.5 % 
    ## 0.006367812 0.035134375

Hence, we will not reject the null hypothesis. But we can observed that
the CI here is a little bit smaller than section b.

#### d

We can use our model in section 4:
*E*\[*Y*\|*w**h**i**t**e* = 1, *c**r**i**m**e* = 1, *b**o**x* = 1\] − *E*\[*Y*\|*w**h**i**t**e* = 0, *c**r**i**m**e* = 1, *b**o**x* = 1\]) − (*E*\[*Y*\|*w**h**i**t**e* = 1, *c**r**i**m**e* = 0, *b**o**x* = 1\] − *E*\[*Y*\|*w**h**i**t**e* = 0, *c**r**i**m**e* = 0, *b**o**x*\]) = *β*<sub>5</sub>

becasue:
(*β*<sub>0</sub> + *β*<sub>1</sub> + *β*<sub>2</sub> + *β*<sub>3</sub> + *β*<sub>4</sub> + *β*<sub>5</sub>) − (*β*<sub>0</sub> + *β*<sub>2</sub> + *β*<sub>4</sub>) − ((*β*<sub>0</sub> + *β*<sub>1</sub> + *β*<sub>2</sub> + *β*<sub>3</sub>) − (*β*<sub>0</sub> + *β*<sub>2</sub>)) = (*β*<sub>1</sub> + *β*<sub>3</sub> + *β*<sub>5</sub>) − (*β*<sub>1</sub> + *β*<sub>3</sub>) = *β*<sub>5</sub>

Thus, our null hypothesis is that *β*<sub>5</sub> = 0, which means that
for employers with a box, the effect of being white vs black is the same
for applicants with vs without a criminal record.

``` r
ex4.model.robust <- coeftest(ex4.model, df = Inf, vcov = vcovHC)
  
  
ex4.model.robust[,c(1,2,3,4)]
```

    ##                         Estimate  Std. Error    z value     Pr(>|z|)
    ## (Intercept)          0.096153846 0.005965649 16.1179179 1.909346e-58
    ## white                0.026853918 0.008928067  3.0078089 2.631386e-03
    ## remover              0.030830281 0.015224955  2.0249834 4.286907e-02
    ## white:remover       -0.015504712 0.021800151 -0.7112204 4.769477e-01
    ## remover:crime       -0.042809043 0.018067344 -2.3694154 1.781623e-02
    ## white:remover:crime -0.007834577 0.025712227 -0.3047024 7.605928e-01

``` r
test_stat <- ex4.model.robust[6,c(1,2,3,4)][3]
test_stat > qt(0.95, dim(df)[1]-1)
```

    ## z value 
    ##   FALSE

``` r
coefci(ex4.model, df = dim(df_ex5)[1]-1, level=.95, vcov = vcovHC)[6,]
```

    ##       2.5 %      97.5 % 
    ## -0.05828110  0.04261194

Hence, we will not reject the null hypothesis (0 is inside the CI, and
the T\_test is smaller the the qt(0.95), df).

#### e

We can use our model in section 4: $ E\[Y\|white=1,box=0\] -
E\[Y\|white=1, crime=0, box=1\] = \_3+\_2$ becasue:
(*β*<sub>0</sub> + *β*<sub>1</sub>) − (*β*<sub>0</sub> + *β*<sub>1</sub> + *β*<sub>2</sub> + *β*<sub>3</sub>) =  − (*β*<sub>2</sub> + *β*<sub>3</sub>)

Thus, our null hypothesis is that *β*<sub>2</sub> + *β*<sub>3</sub> = 0,
which means that foremployers with no box is the same as the effect of
being white for applicants with no criminal record applying to employers
with a box.

``` r
x <- ex4.model$coefficients[3]+ex4.model$coefficients[4]
se <- summary(ex4.model)$coefficients[3,2]+summary(ex4.model)$coefficients[4,2]

L <- x - se*qt(0.95, dim(df)[1]-1)
U <- x + se*qt(0.95, dim(df)[1]-1)
print(c(L, U))
```

    ##     remover     remover 
    ## -0.04202087  0.07267201

``` r
# ex4.model.robust[,c(1,2,3,4)]
# test_stat <- ex4.model.robust[3,c(1,2,3,4)][3]+ex4.model.robust[4,c(1,2,3,4)][3]
# test_stat > qt(0.95, dim(df_ex4)[1]-1)
# coefci(ex4.model, df = dim(df_ex5)[1]-1, level=.95, vcov = vcovHC)[3,]
```

We can see that 0 is inside the CI, hence we will not reject the null
hypothesis with a 5% signifcance level.

#### f

As we saw in class, for joint null hypothesis we can use the Wald
chi-square method to test our joint null hypothesis. We want to Test the
joint null of no effect of being white on callbacks for employers
without a box. Which means that is the joint null hypothesis of d+e:
{*β*<sub>5</sub> = 0} ∩ {*β*<sub>2</sub> + *β*<sub>3</sub> = 0}

``` r
print(linearHypothesis(ex4.model,c("white:remover:crime=0","remover + white:remover=0"), test = "Chisq",
vcov=vcovHC,df = Inf))
```

    ## Linear hypothesis test
    ## 
    ## Hypothesis:
    ## white:remover:crime = 0
    ## remover  + white:remover = 0
    ## 
    ## Model 1: restricted model
    ## Model 2: response ~ white + remover + (white:remover) + (crime:remover) + 
    ##     (white:crime:remover)
    ## 
    ## Note: Coefficient covariance matrix supplied.
    ## 
    ##   Res.Df Df  Chisq Pr(>Chisq)
    ## 1   7241                     
    ## 2   7239  2 1.0094     0.6037

We can see that we got pretty high p.value (0.6037) so we will not
reject the null hypothesis.

### 6

#### a.

*C**a**l**l**b**a**c**k*<sub>*i*</sub> = *β*<sub>0</sub> + *β*<sub>1</sub>*W**h**i**t**e*<sub>*i*</sub> + *β*<sub>2</sub>*B**o**x*<sub>*i*</sub> + *β*<sub>3</sub>(*W**h**i**t**e*<sub>*i*</sub> × *B**o**x*<sub>*i*</sub>) + *β*<sub>4</sub>(*C**r**i**m**e*<sub>*i*</sub> × *B**o**x*<sub>*i*</sub>) + *β*<sub>5</sub>(*W**h**i**t**e*<sub>*i*</sub> × *C**r**i**m**e*<sub>*i*</sub> × *B**o**x*<sub>*i*</sub>) + *β*<sub>6</sub>*G**E**D*<sub>*i*</sub> + *β*<sub>7</sub>*E**m**p**l**o**y**m**e**n**t**G**a**p*<sub>*i*</sub> + *ϵ*<sub>*i*</sub>
We saw that OLS estimation of the regression model of equation 3 is
equivalent to OLS estimation of the regression model of equation 1 on
employers without a box and separately OLS estimation of the regression
model of equation 2 on employers with a box. Is the same statement true
for OLS estimation of equation 4? If not, what changes would you have to
make to equation 4 for the statement to be true? would there be
disadvantages of making those changes?

The connection of the 1st, 2nd and 3rd model. The same statement isnt
true for that model, because in model 4 we added variables that arent in
any of the 1st and 2nd model (GED and Employment Gap). We will have to
add more interaction estimators for any of the variables that are in the
1st and 2nd model (Particularly the box). Which means that we will have
alot of coefficients for this model. The model will be complicated and
it will be very hard to make a good inference.

#### b+c.

For the regression models of equations 1 - 3, we saw that the estimated
parameters are linear combinations of conditional sample means, and the
resulting fitted values are conditional sample means. Are the same
statements true for the regression model of equation 4? If not, what
changes would you have to make to equation 4 for the statements to be
true? would there be disadvantages of making those changes?

``` r
df_ex4 <- subset(df, pre==1)
ex6.model <- lm(data=df_ex4, response ~ white + remover + (white:remover) +(crime:remover)+(white:crime:remover) +ged +empgap +(ged*remover)+(empgap*remover))

summary(ex6.model)
```

    ## 
    ## Call:
    ## lm(formula = response ~ white + remover + (white:remover) + (crime:remover) + 
    ##     (white:crime:remover) + ged + empgap + (ged * remover) + 
    ##     (empgap * remover), data = df_ex4)
    ## 
    ## Residuals:
    ##     Min      1Q  Median      3Q     Max 
    ## -0.1467 -0.1268 -0.1057 -0.0863  0.9239 
    ## 
    ## Coefficients:
    ##                      Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)          0.105692   0.008879  11.904  < 2e-16 ***
    ## white                0.027045   0.008919   3.032  0.00244 ** 
    ## remover              0.012820   0.018635   0.688  0.49149    
    ## ged                 -0.013439   0.008920  -1.507  0.13196    
    ## empgap              -0.005952   0.008920  -0.667  0.50463    
    ## white:remover       -0.015538   0.020342  -0.764  0.44499    
    ## remover:crime       -0.042396   0.018341  -2.312  0.02083 *  
    ## remover:ged          0.018151   0.015692   1.157  0.24744    
    ## remover:empgap       0.017900   0.015678   1.142  0.25359    
    ## white:remover:crime -0.007835   0.025738  -0.304  0.76082    
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1
    ## 
    ## Residual standard error: 0.3119 on 7235 degrees of freedom
    ## Multiple R-squared:  0.00364,    Adjusted R-squared:  0.0024 
    ## F-statistic: 2.937 on 9 and 7235 DF,  p-value: 0.00176

#### d.

``` r
betas.3 =data.frame(model4 = betas_3)
betas.4 = round(data.frame(model6 = ex6.model$coefficients),3)
ans_6 <- merge(betas.4, betas.3, by=0, all=TRUE)[-1,]
# ans_6[is.na(ans_6)] <- 0
ans_6$diff <- ans_6$model6 - ans_6$model4
ans_6
```

    ##              Row.names model6 model4   diff
    ## 2               empgap -0.006     NA     NA
    ## 3                  ged -0.013     NA     NA
    ## 4              remover  0.013  0.031 -0.018
    ## 5        remover:crime -0.042 -0.043  0.001
    ## 6       remover:empgap  0.018     NA     NA
    ## 7          remover:ged  0.018     NA     NA
    ## 8                white  0.027  0.027  0.000
    ## 9        white:remover -0.016 -0.016  0.000
    ## 10 white:remover:crime -0.008 -0.008  0.000

It looks like the main coefficients that changed are the intercept and
the box. GED and EmploymentGap also affect employers’ decision over
whether to hire someone. That is, the employer’s decision whether to
hire someone for the job is biased by the box, GED, EmploymentGap and
probably other variables. So, those variables were the ommited variable
bias for the older models, and here we can see that the estimate of the
box is smaller than model 3m
