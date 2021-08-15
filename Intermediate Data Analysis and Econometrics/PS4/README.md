PS4
================
Yonatan-Lourie
6/9/2021

In this problem set, we will look at DufLo et al. (2011). This paper
looks at the effect of tracking on educational attainment. “Tracking”
means that high-performing and low-performing students are separated
into two sections. The authors run experiment in which they randomly
assign 60 out of 120 primary schools in rural Kenya to tracking. In
Table 5, the authors explore the effects of being in the bottom section
of a tracking school. The problem set will focus mostly on replicating
this table. Some of your estimates will not be exactly the same as those
in Table 5 of the paper, but they should be very close. The name of the
dataset is forpeerpapers.dta. tracking is a dummy variable that denotes
whether the student is a tracking school. bottomhalf denotes the dummy
variable Bij which is 1 if the student was assigned to the bottom
section and 0 otherwise, percentile denotes Pij which is the student’s
test score percentile at baseline, etpteacher denotes the type of
teacher, girl denotes gender, agetest denotes age, and stdR\_totalscore
is standardized test scores.

## Part 1. Introduction

### 1.

Read the introduction of the paper. List three channels through which
tracking might affect students, according to the authors.

1.  Level of studies: Tracking might help the teacher to teach each
    group in the level of studies that fit to that specific group
    (basics for low abilities students, and intermediate materials for
    high abilities students for example).

2.  Peer to peer effect: Better students can have a positive impact on
    worse students. When we divide students into groups according to
    their abilities - we prevent the possibility of the bad students to
    get better by interacting with better students.

Tracking students into separate classes by prior achievement could
disadvantage low-achieving students while benefiting high-achieving
students, thereby exacerbating inequality. if a student is in the
low-achieving group, he dont have the opportunity to excel as much as
the high-achieving students, because he will not have the right platform
(the level of the class).

3.  The effort of the teacher could be vary by the group of the
    students. Their are teachers that will invest more effort on a
    low-achieving group, and their are teachers that will invest more
    effort on a high-achieving group, depend on the teacher. This will
    have a direct influence on the students of course.

## Part 2. Regression Discontinuity

In Panel A of Table 5, the authors use a regression discontinuity
approach to explore the effects of being in the bottom section of a
tracking school on test scores.

### 1.

Let’s start with Specification 1. This is a classic regression
discontinuity, in which the authors estimate the below equation, where
*β*<sub>*i**j*</sub> is a dummy for whether or not individual i in
school j is in the bottom section, *P*<sub>*i**j*</sub> is the student’s
test score percentile at baseline, and *X*<sub>*i**j*</sub> is a vector
of controls (including a constant, type of teacher, gender, and age at
time of test).

(1)  *y*<sub>*i**j*</sub> = *δ**B*<sub>*i**j*</sub> + *λ*<sub>1</sub>*P*<sub>*i**j*</sub> + *λ*<sub>2</sub>*P*<sub>*i**j*</sub><sup>2</sup> + *λ*<sub>3</sub>*P*<sub>*i**j*</sub><sup>3</sup> + *X*<sub>*i**j*</sub>*β* + *ϵ*<sub>*i**j*</sub>

#### (a)

Explain why δ is a plausible causal estimate of the effect of being in
the bottom section on educational attainment:

As we learned in the RD class, when the dummy will be *B* = 0, our
*y*<sub>*i**j*</sub> will be the score of someone that is not in the
bottom section. And if *B* = 1, y will be the score of someone that is
in the bottom section.

*E*(*Y*\|*B* = 1, *P*, *X*) − *E*(*Y*\|*B* = 0, *P*, *X*) = *δ*

#### (b)

Load the data and create a new data frame that includes tracking schools
only (tracking==1). You will use this data frame for the rest of the
problem set.

``` r
library(tidyverse)
library(sandwich)
library(lmtest)
library(car)
library(haven)
library(stargazer)
library(AER)
set.seed(42)
```

``` r
df_ <- read_dta("forpeerpapers.dta")
df <- subset(df_, tracking==1)
df$constant <- 1
head(df)
```

    ## # A tibble: 6 x 124
    ##   pupilid schoolid district bungoma division zone  tracking   sbm  girl agetest
    ##     <dbl>    <dbl> <chr>      <dbl> <chr>    <chr>    <dbl> <dbl> <dbl>   <dbl>
    ## 1 4301001      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     0       7
    ## 2 4301002      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     1      12
    ## 3 4301003      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     1       8
    ## 4 4301004      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     0      14
    ## 5 4301005      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     0      11
    ## 6 4301007      430 BUNGOMA        1 KANDUYI  MUNI~        1     1     0      10
    ## # ... with 114 more variables: etpteacher <dbl>, lowstream <dbl>,
    ## #   stream_meanpercentile <dbl>, SDstream_std_mark <dbl>,
    ## #   MEANstream_std_mark <dbl>, bottomhalf <dbl>, tophalf <dbl>,
    ## #   bottomquarter <dbl>, secondquarter <dbl>, thirdquarter <dbl>,
    ## #   topquarter <dbl>, std_mark <dbl>, percentile <dbl>, realpercentile <dbl>,
    ## #   quantile5p <dbl>, attrition <dbl>, w_correct <dbl>, w_incorrect <dbl>,
    ## #   w_missing <dbl>, w_blanks <chr>, s_correct <dbl>, s_incorrect <dbl>,
    ## #   s_missing <dbl>, s_blanks <chr>, a1_correct <dbl>, a2_correct <dbl>,
    ## #   a3_correct <dbl>, a4_correct <dbl>, a5_correct <dbl>, a6_correct <dbl>,
    ## #   a7_correct <dbl>, a8_correct <dbl>, spelling_correct <dbl>, l_hr <dbl>,
    ## #   l_min <dbl>, l_tried <dbl>, l_errors <dbl>, l_time <dbl>, wordscore <dbl>,
    ## #   sentscore <dbl>, letterscore <dbl>, letterscoreraw <dbl>, spellscore <dbl>,
    ## #   sentscore24 <dbl>, letterscore24 <dbl>, spellscore24 <dbl>, litscore <dbl>,
    ## #   additions_score <dbl>, substractions_score <dbl>,
    ## #   multiplications_score <dbl>, mathscoreraw <dbl>, totalscore <dbl>,
    ## #   rMEANstream_std_baselinemark <dbl>, rSDstream_std_baselinemark <dbl>,
    ## #   rMEANstream_std_total <dbl>, rSDstream_std_total <dbl>,
    ## #   rMEANstream_std_math <dbl>, rSDstream_std_math <dbl>,
    ## #   rMEANstream_std_lit <dbl>, rSDstream_std_lit <dbl>, r2_attrition <dbl>,
    ## #   r2_age <dbl>, r2_w_correct <dbl>, r2_w_incorrect <dbl>, r2_w_missing <dbl>,
    ## #   r2_s_correct <dbl>, r2_s_incorrect <dbl>, r2_s_missing <dbl>,
    ## #   r2_a1_correct <dbl>, r2_a2_correct <dbl>, r2_a3_correct <dbl>,
    ## #   r2_a4_correct <dbl>, r2_a5_correct <dbl>, r2_a6_correct <dbl>,
    ## #   r2_a7_correct <dbl>, r2_a8_correct <dbl>, r2_spelling_correct <dbl>,
    ## #   r2_l_hr <dbl>, r2_l_min <dbl>, r2_l_tried <dbl>, r2_l_errors <dbl>,
    ## #   r2_l_time <dbl>, r2_wordscore <dbl>, r2_sentscore <dbl>,
    ## #   r2_letterscore <dbl>, r2_letterscoreraw <dbl>, r2_spellscore <dbl>,
    ## #   r2_sentscore24 <dbl>, r2_letterscore24 <dbl>, r2_spellscore24 <dbl>,
    ## #   r2_litscore <dbl>, r2_mathscoreraw <dbl>, r2_additions_score <dbl>,
    ## #   r2_substractions_score <dbl>, r2_multiplications_score <dbl>,
    ## #   r2_totalscore <dbl>, etpteacher_tracking_lowstream <dbl>,
    ## #   sbm_tracking_lowstream <dbl>, bottomhalf_tracking <dbl>,
    ## #   tophalf_tracking <dbl>, ...

#### (c)

Create variables P2 and P3 and estimate Equation 1 to replicate the
coefficient in Column 1.

``` r
model.1 <- lm(data = df, stdR_totalscore~ +bottomhalf+percentile+percentilesq+percentilecub+(etpteacher+girl+agetest))

P2 <- coef(model.1)[3]
P3 <- coef(model.1)[4]
coef(summary(model.1))["bottomhalf",c("Estimate","Std. Error")]
```

    ##    Estimate  Std. Error 
    ## 0.009979103 0.086000813

``` r
round(coef(summary(model.1)),4)
```

    ##               Estimate Std. Error t value Pr(>|t|)
    ## (Intercept)    -0.5186     0.1438 -3.6058   0.0003
    ## bottomhalf      0.0100     0.0860  0.1160   0.9076
    ## percentile      0.0334     0.0069  4.8745   0.0000
    ## percentilesq   -0.0005     0.0002 -2.7205   0.0066
    ## percentilecub   0.0000     0.0000  3.1701   0.0015
    ## etpteacher      0.2068     0.0323  6.3988   0.0000
    ## girl            0.1112     0.0326  3.4131   0.0007
    ## agetest        -0.0527     0.0114 -4.6219   0.0000

#### (d)

Estimate the standard error of δ^, using bootstrap and clustering at the
school level.1 (Hint: Code for doing this using loops has been provided
for you on moodle since this is a hard problem. You can also try using
the package multiwayvcov and the function cluster.boot.)

``` r
MyBootstrap <- function(df, B, p, formula, fm0) {
  formula = as.formula(formula)
  
  set.seed(42)
  # Create a vector with schools IDs.
  schools <- unique(df$schoolid)
  
  # Create a matrix to store our estimates
  mat <- matrix(NA, nrow = B, ncol = p)
  
  # Loop over bootstrap repetitions
  for (b in 1:B) {
    # Randomly choose schools
    i <- sample(1:length(schools), length(schools), replace = TRUE)
    
    # Which school are included?
    inc_school <- schools[i]
    
    # Collect data only for the included schools
    for(j in 1:length(schools)) {
      if (j == 1) {
        # Select the school
        df_boot <- df[which(df$schoolid == inc_school[j]), ]
        
        # Create a new school index (because we treat each bootstraped 
        # school as a different school)
        df_boot$new <- j
        
      } else {
        # Select the school
        temp <- df[which(df$schoolid == inc_school[j]), ]
        
        # Create a new school index (because we treat each bootstraped
        # school as a different school)
        temp$new <- j
        
        # Vertically merge dataframes
        df_boot <- rbind(df_boot, temp)
      }
    }
    
    # Run the desired model using the bootstrapped dataset
    model <- lm(
      formula,
      data = df_boot
    )
    
    # Store the desired coefficiient
    mat[b, ] <- model$coefficients
  }
  
  # Report the results
  c5 <- coeftest(fm0, df = Inf, vcov. = cov(mat))
  c5[2,]
  
  return(round(c5,4))

}
```

``` r
boot1 <- MyBootstrap(df,500,8,"stdR_totalscore ~ bottomhalf + percentile + percentilesq +
      percentilecub + etpteacher + girl + agetest",model.1)
boot1
```

    ## 
    ## z test of coefficients:
    ## 
    ##               Estimate Std. Error z value Pr(>|z|)    
    ## (Intercept)    -0.5186     0.1999 -2.5947   0.0095 ** 
    ## bottomhalf      0.0100     0.0938  0.1063   0.9153    
    ## percentile      0.0334     0.0057  5.8145   <2e-16 ***
    ## percentilesq   -0.0005     0.0001 -3.2750   0.0011 ** 
    ## percentilecub   0.0000     0.0000  3.9144   0.0001 ***
    ## etpteacher      0.2068     0.0562  3.6783   0.0002 ***
    ## girl            0.1112     0.0366  3.0396   0.0024 ** 
    ## agetest        -0.0527     0.0167 -3.1524   0.0016 ** 
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

So the standard error of *δ̂* is 0.0938

#### (e)

Explain why school fixed effects might be especially important in this
context (keeping in mind your answer to (a)). Replicate the coefficient
in Column 2 by adding school fixed effects to the model in Equation 1.
Report your results omitting the coefficients on the school dummies.

(1)  *y*<sub>*i**j*</sub> = *δ**B*<sub>*i**j*</sub> + *λ*<sub>1</sub>*P*<sub>*i**j*</sub> + *λ*<sub>2</sub>*P*<sub>*i**j*</sub><sup>2</sup> + *λ*<sub>3</sub>*P*<sub>*i**j*</sub><sup>3</sup> + *X*<sub>*i**j*</sub>*β* + *ϵ*<sub>*i**j*</sub> + *ν*<sub>*j*</sub>

``` r
model.2 <- lm(data = df, stdR_totalscore~ bottomhalf +percentile+percentilesq+percentilecub+(etpteacher+girl+agetest)+factor(schoolid)-1)
print("Column (2) in Table 5")
```

    ## [1] "Column (2) in Table 5"

``` r
coef(summary(model.2))["bottomhalf",c("Estimate","Std. Error")]
```

    ##    Estimate  Std. Error 
    ## 0.001438926 0.076098312

``` r
print("All the coefficients")
```

    ## [1] "All the coefficients"

``` r
round(coef(summary(model.2))[1:7,],4)
```

    ##               Estimate Std. Error t value Pr(>|t|)
    ## bottomhalf      0.0014     0.0761  0.0189   0.9849
    ## percentile      0.0343     0.0061  5.6425   0.0000
    ## percentilesq   -0.0005     0.0002 -3.2002   0.0014
    ## percentilecub   0.0000     0.0000  3.6761   0.0002
    ## etpteacher      0.1854     0.0288  6.4364   0.0000
    ## girl            0.1237     0.0291  4.2578   0.0000
    ## agetest        -0.0455     0.0107 -4.2532   0.0000

So our estimate for bottomhalf is really similar to the one in Table 5

school fixed effects will reflect the causal effect of peers’ prior
achievement (both direct through peer-to-peer learning, and indirect
through adjustment in teacher behavior to the extent to which teachers
change behavior in response to small random variations in class
composition).

#### (f)

Compute the cluster standard error for *δ̂* using the bootstrap. Note
that, when running this regression on the bootstrapped sample you must
use the new school indexes (df\_boot$new in my code) to generate your
school fixed effects

``` r
boot2 <- MyBootstrap(df,500,67,"stdR_totalscore~ bottomhalf + percentile + percentilesq +
      percentilecub + etpteacher + girl + agetest+ factor(new)-1",model.2)
boot2[1:7,]
```

    ##               Estimate Std. Error z value Pr(>|z|)
    ## bottomhalf      0.0014     0.0840  0.0171   0.9863
    ## percentile      0.0343     0.0054  6.3679   0.0000
    ## percentilesq   -0.0005     0.0001 -3.7332   0.0002
    ## percentilecub   0.0000     0.0000  4.3846   0.0000
    ## etpteacher      0.1854     0.0520  3.5654   0.0004
    ## girl            0.1237     0.0347  3.5682   0.0004
    ## agetest        -0.0455     0.0107 -4.2712   0.0000

So we got *s**t**d*(*δ̂*)= 0.084

### 2.

Now let’s move on to Specification 2. Here, the authors estimate
something similar to Equation 1, except this time, they “estimate a
second order polynomial separately on each side of the discontinuity”
(p. 1754). This amounts to estimating the following equation (adding the
terms in red to Equation 1)

(2)  *y*<sub>*i**j*</sub> = *δ**B*<sub>*i**j*</sub> + *λ*<sub>1</sub>*P*<sub>*i**j*</sub> + *λ*<sub>2</sub>*P*<sub>*i**j*</sub><sup>2</sup> + *ϕ**P*<sub>*i**j*</sub> \* *β*<sub>*i**j*</sub> + *ϕ**P*<sub>*i**j*</sub><sup>2</sup> \* *β*<sub>*i**j*</sub> + *X*<sub>*i**j*</sub>*β* + *ϵ*<sub>*i**j*</sub>

#### (a)

Explain why this might change our estimated effect of being in the
bottom section.

*ϕ* is actually the complement of the bottom half (the top half). When
we adding an interaction term (we adding for P and P^2), then the effect
of having some grade in the top section will be shown as part of *ϕ*.
Before that, those effects was observed in other covariates (and
probably in *δ* to), so it will effect *δ*.

#### (b)

Create the necessary variables and estimate the model in Equation 2.

``` r
df$percentile_50 <- df$percentile-50
df$percentile_50_sq <- (df$percentile-50)^2
model.3 <- lm(data = df, stdR_totalscore ~ bottomhalf+percentile_50+percentile_50_sq+bottomhalf:percentile_50+bottomhalf:percentile_50_sq+I(etpteacher+girl+agetest))
summary(model.3)
```

    ## 
    ## Call:
    ## lm(formula = stdR_totalscore ~ bottomhalf + percentile_50 + percentile_50_sq + 
    ##     bottomhalf:percentile_50 + bottomhalf:percentile_50_sq + 
    ##     I(etpteacher + girl + agetest), data = df)
    ## 
    ## Residuals:
    ##     Min      1Q  Median      3Q     Max 
    ## -2.2972 -0.6524 -0.1453  0.5629  3.2117 
    ## 
    ## Coefficients:
    ##                                  Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)                     0.3690481  0.1323984   2.787  0.00535 ** 
    ## bottomhalf                     -0.0449015  0.0982680  -0.457  0.64776    
    ## percentile_50                  -0.0063744  0.0065852  -0.968  0.33313    
    ## percentile_50_sq                0.0005842  0.0001265   4.620    4e-06 ***
    ## I(etpteacher + girl + agetest) -0.0149670  0.0106748  -1.402  0.16100    
    ## bottomhalf:percentile_50        0.0255026  0.0091446   2.789  0.00532 ** 
    ## bottomhalf:percentile_50_sq    -0.0005540  0.0001789  -3.096  0.00198 ** 
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1
    ## 
    ## Residual standard error: 0.8868 on 2952 degrees of freedom
    ##   (654 observations deleted due to missingness)
    ## Multiple R-squared:  0.2423, Adjusted R-squared:  0.2407 
    ## F-statistic: 157.3 on 6 and 2952 DF,  p-value: < 2.2e-16

#### (c)

Replicate the coefficient in Column 3 by predicting the effect of being
in the bottom section for a student at the 50th percentile.

*E*(*y*\|*B* = 1, *P* = 50) = *E*(*y*\|*B* = 0, *P* = 50) = *i**n**t**e**r**c**e**p**t* + *δ* + *λ*<sub>1</sub> + *λ*<sub>2</sub> + *ϕ*<sub>1</sub> + *ϕ*<sub>2</sub> + *β* − \[*i**n**t**e**r**c**e**p**t* + *λ*<sub>1</sub> + *λ*<sub>2</sub> + *β*\] = *δ* + *ϕ*<sub>1</sub>(50 − 50) + *ϕ*<sub>2</sub>(50 − 50)<sup>2</sup> = *δ*

``` r
coef(summary(model.3))[2,]
```

    ##    Estimate  Std. Error     t value    Pr(>|t|) 
    ## -0.04490155  0.09826799 -0.45692953  0.64775532

We got -0.0449015 which is similar to what we have in column 3.

#### (d)

Compute clustered standard errors using the bootstrap by adapting the
code provided.

``` r
boot3 <- MyBootstrap(df,500,7,"stdR_totalscore ~ bottomhalf+percentile_50+percentile_50_sq+bottomhalf:percentile_50+bottomhalf:percentile_50_sq+I(etpteacher+girl+agetest)",model.3)
round(boot3,4)
```

    ## 
    ## z test of coefficients:
    ## 
    ##                                Estimate Std. Error z value Pr(>|z|)    
    ## (Intercept)                      0.3690     0.1788  2.0638   0.0390 *  
    ## bottomhalf                      -0.0449     0.1101 -0.4077   0.6835    
    ## percentile_50                   -0.0064     0.0061 -1.0368   0.2998    
    ## percentile_50_sq                 0.0006     0.0001  5.5214   <2e-16 ***
    ## I(etpteacher + girl + agetest)  -0.0150     0.0165 -0.9076   0.3641    
    ## bottomhalf:percentile_50         0.0255     0.0077  3.3175   0.0009 ***
    ## bottomhalf:percentile_50_sq     -0.0006     0.0001 -3.7452   0.0002 ***
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

## Part 3. Instrumental Variables

In Panels B and C of Table 5, the authors examine one channel by which
being assigned to the bottom section might affect attainment: low peer
quality. To do this, they use instrumental variables. The first stage
will regress mean peer score (y¯−ij) on being in the bottom half, which
is plausibly exogenous when we include controls for the fLexible
polynomial of baseline attainment, for the reasons discussed in 1a.

The second stage will regress endline scores on mean peer scores, using
the predicted values of mean peer score from the first stage regression.

(3)  *ȳ*<sub> − *i**j*</sub> = *δ**B*<sub>*i**j*</sub> + *λ*<sub>1</sub>*P*<sub>*i**j*</sub> + *λ*<sub>2</sub>*P*<sub>*i**j*</sub><sup>2</sup> + *λ*<sub>3</sub>*P*<sub>*i**j*</sub><sup>3</sup> + *X*<sub>*i**j*</sub>*β* + *ϵ*<sub>*i**j*</sub>

### 1.

First we will replicate the first stage regression in Panel C Column 1

#### (a)

Create a new data frame that contains the subset of observations for
which none of the variables in the model are missing. (Hint: Use na.omit
and subset with the option select.)

``` r
df3 <- na.omit(subset(df, select=c(rMEANstream_std_total,stdR_totalscore, bottomhalf, percentile,percentilesq,percentilecub, etpteacher,girl,agetest,schoolid)))
```

#### (b)

Estimate the first stage by estimating Equation 3 with
rMEANstream\_std\_total as your outcome variable.

``` r
model.4 <- lm(data = df3, rMEANstream_std_total ~ bottomhalf+percentile+percentilesq+percentilecub+(etpteacher+girl+agetest))
round(coef(summary(model.4)),4)
```

    ##               Estimate Std. Error  t value Pr(>|t|)
    ## (Intercept)     0.3057     0.0325   9.3996   0.0000
    ## bottomhalf     -0.7921     0.0194 -40.7245   0.0000
    ## percentile     -0.0059     0.0016  -3.7971   0.0001
    ## percentilesq    0.0002     0.0000   4.0590   0.0001
    ## percentilecub   0.0000     0.0000  -4.0072   0.0001
    ## etpteacher      0.1567     0.0073  21.4329   0.0000
    ## girl            0.0046     0.0074   0.6222   0.5339
    ## agetest         0.0022     0.0026   0.8491   0.3959

#### (c)

Compute clustered standard errors using the bootstrap by adapting the
code provided

``` r
boot4 <- MyBootstrap(df3,500,8,"rMEANstream_std_total ~ bottomhalf+percentile+percentilesq+percentilecub+(etpteacher+girl+agetest)",model.4)
boot4
```

    ## 
    ## z test of coefficients:
    ## 
    ##               Estimate Std. Error  z value Pr(>|z|)    
    ## (Intercept)     0.3057     0.0386   7.9265   <2e-16 ***
    ## bottomhalf     -0.7921     0.0411 -19.2860   <2e-16 ***
    ## percentile     -0.0059     0.0014  -4.0676   <2e-16 ***
    ## percentilesq    0.0002     0.0000   4.2483   <2e-16 ***
    ## percentilecub   0.0000     0.0000  -4.2571   <2e-16 ***
    ## etpteacher      0.1567     0.0453   3.4600   0.0005 ***
    ## girl            0.0046     0.0079   0.5793   0.5624    
    ## agetest         0.0022     0.0034   0.6477   0.5172    
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

### 2.

Now let’s replicate the second stage regression in Panel B Column 1.
\#\#\#\# (a) First, do this “manually” by estimating the following:

(3)  *y*<sub>*i**j*</sub> = *δ**ŷ*<sub> − *i**j*</sub> + *λ*<sub>1</sub>*P*<sub>*i**j*</sub> + *λ*<sub>2</sub>*P*<sub>*i**j*</sub><sup>2</sup> + *λ*<sub>3</sub>*P*<sub>*i**j*</sub><sup>3</sup> + *X*<sub>*i**j*</sub>*β* + *U*<sub>*i**j*</sub>
where y^−ij are the predicted values of y¯−ij from the first stage
regression. To estimate y^−ij in R, you can use
firststage$fitted.values, where firststage is the object that stores the
first stage regression.

``` r
df3$firststage <-  model.4$fitted.values
model.5 <- lm(data = df3, stdR_totalscore ~ firststage+percentile+percentilesq+percentilecub+(etpteacher+girl+agetest))
round(coeftest(model.5, vcov = vcovHC),4)
```

    ## 
    ## t test of coefficients:
    ## 
    ##               Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)    -0.5147     0.1288 -3.9967   0.0001 ***
    ## firststage     -0.0126     0.1087 -0.1159   0.9077    
    ## percentile      0.0334     0.0070  4.7654   <2e-16 ***
    ## percentilesq   -0.0005     0.0002 -2.5610   0.0105 *  
    ## percentilecub   0.0000     0.0000  2.9666   0.0030 ** 
    ## etpteacher      0.2088     0.0365  5.7142   <2e-16 ***
    ## girl            0.1113     0.0328  3.3936   0.0007 ***
    ## agetest        -0.0527     0.0115 -4.5688   <2e-16 ***
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

#### (b)

Second, do this using ivreg from the AER package. Type ?ivreg in the
console to look up the syntax. You should replicate your coefficient
from (a) exactly (but not the standard error).

``` r
model.6 <- ivreg(stdR_totalscore~rMEANstream_std_total+ percentile+percentilesq+percentilecub + (etpteacher+girl+agetest)|bottomhalf+percentile+percentilesq+percentilecub + (etpteacher+girl+agetest), data=df3)



round(coeftest(model.6, vcov = vcovHC),4)
```

    ## 
    ## t test of coefficients:
    ## 
    ##                       Estimate Std. Error t value Pr(>|t|)    
    ## (Intercept)            -0.5147     0.1288 -3.9954   0.0001 ***
    ## rMEANstream_std_total  -0.0126     0.1087 -0.1159   0.9078    
    ## percentile              0.0334     0.0070  4.7638   <2e-16 ***
    ## percentilesq           -0.0005     0.0002 -2.5603   0.0105 *  
    ## percentilecub           0.0000     0.0000  2.9658   0.0030 ** 
    ## etpteacher              0.2088     0.0366  5.7128   <2e-16 ***
    ## girl                    0.1113     0.0328  3.3927   0.0007 ***
    ## agetest                -0.0527     0.0115 -4.5672   <2e-16 ***
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

#### (c)

Compute clustered standard errors using the bootstrap by adapting the
code provided

``` r
boot5 <- MyBootstrap(df3,500,5,"stdR_totalscore ~ firststage+percentile+percentilesq+percentilecub+I(etpteacher+girl+agetest)-1",model.5)
boot5
```

    ## 
    ## z test of coefficients:
    ## 
    ##               Estimate Std. Error   z value Pr(>|z|)    
    ## (Intercept)    -0.5147     0.1395   -3.6911   0.0002 ***
    ## firststage     -0.0126     0.0067   -1.8847   0.0595 .  
    ## percentile      0.0334     0.0002  181.2722   <2e-16 ***
    ## percentilesq   -0.0005     0.0000 -409.2875   <2e-16 ***
    ## percentilecub   0.0000     0.0076    0.0005   0.9996    
    ## etpteacher      0.2088     0.1395    1.4974   0.1343    
    ## girl            0.1113     0.0067   16.6451   <2e-16 ***
    ## agetest        -0.0527     0.0002 -286.5025   <2e-16 ***
    ## ---
    ## Signif. codes:  0 '***' 0.001 '**' 0.01 '*' 0.05 '.' 0.1 ' ' 1

### 3.

When using an instrumental variable, one of the identification
assumptions is that the instrument is exogenous: Cov(Bij; Uij) = 0. In
plain English, this assumption imposes that being assigned to the bottom
session only impacts your tests scores by changing the quality of your
peers (y−ij). As consequence, the exogeneity assumption implies that
that there is no direct impact of being assigned to the bottom session
on your own test scores. Do you believe this assumption is plausible?
Think about what unobservables variables are inside the term Uij or
about possible direct effects of being assigned to a bottom session.

There is (almost) always more covariates that can affect the assumption
that the instrument is exogenous (the question is how significance). In
our case, it could be: 1. Low self esteem that will lead to lower test
score. 2. Dislection or other learning disability that havent
diagonsted. 3. Lack of family support etc..

The more covraites that we will add to our regression, we can be more
sure that *C**o**v*(*B*<sub>*i**j*</sub>, *U*<sub>*i**j*</sub>) closer
to zero.
