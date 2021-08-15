PS2
================
Yonatan-Lourie
4/25/2021

``` r
setwd(dirname(rstudioapi::getActiveDocumentContext()$path))
       
knitr::opts_chunk$set(warning=FALSE)
```

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
library(readstata13)
library(gridExtra)
library(sandwich)
library(lmtest)
```

$$
(2) \\ \\ E\\left(r\_{j}\\right)-r\_{f}=\\beta\_{j}\\left(E\\left(r\_{M}\\right)-r\_{f}\\right) \\ \\ \\ S.t: \\ \\ 
\\beta\_j = \\frac{cov(r\_j, r\_M)}{var(r\_M)}
$$

## Part 1. CAPM -*β* and Understanding OLS

### 1.

``` r
df <- read.dta13("data_part1.dta")
df$rf <- 0.0041
df$rM_rf <- df$r_M - df$rf
df$rA_rf <- df$r_A - df$rf
df$rB_rf <- df$r_B - df$rf
head(df)
```

    ##        date gspc_sp500 mcd_mcdonalds gld_spdrgoldshares gnw_genworthfinancial
    ## 1 12/1/2004    1173.78         30.90              45.28                 26.40
    ## 2  1/1/2005    1211.92         31.60              42.98                 27.00
    ## 3  2/1/2005    1181.27         32.05              42.09                 26.54
    ## 4  3/1/2005    1203.60         33.02              43.30                 28.20
    ## 5  4/1/2005    1180.59         31.20              42.60                 27.65
    ## 6  5/1/2005    1156.85         29.12              43.13                 28.00
    ##   ms_morganstanley         r_M r_mcd_mcdonalds         r_A         r_C
    ## 1            51.00  0.03855962      0.06368332  0.01913118  0.09999999
    ## 2            55.97  0.03249332      0.02265375 -0.05079504  0.02272729
    ## 3            56.45 -0.02529047      0.01424047 -0.02070729 -0.01703700
    ## 4            56.47  0.01890335      0.03026525  0.02874790  0.06254709
    ## 5            57.48 -0.01911766     -0.05511810 -0.01616630 -0.01950359
    ## 6            50.30 -0.02010858     -0.06666666  0.01244138  0.01265824
    ##             r_B     rf       rM_rf        rA_rf        rB_rf
    ## 1 -0.0058479384 0.0041  0.03445962  0.015031184 -0.009947938
    ## 2  0.0974510014 0.0041  0.02839332 -0.054895037  0.093351001
    ## 3  0.0085760141 0.0041 -0.02939047 -0.024807292  0.004476014
    ## 4  0.0003543039 0.0041  0.01480335  0.024647899 -0.003745696
    ## 5  0.0178855732 0.0041 -0.02321766 -0.020266300  0.013785573
    ## 6 -0.1249130219 0.0041 -0.02420858  0.008341376 -0.129013022

### 2. sample variance of (*r*<sub>*A*</sub>; *r*<sub>*B*</sub>; *r*<sub>*M*</sub>) , and separately of (*r*<sub>*A*</sub> − *r*<sub>*f*</sub>; *r*<sub>*B*</sub> − *r*<sub>*f*</sub>; *r*<sub>*M*</sub> − *r*<sub>*f*</sub>)

``` r
var1 <- cov(df[c("r_A", "r_B", "r_M")])
var2 <- cov(df[c("rA_rf", "rB_rf", "rM_rf")])


#or maybe should be:
# thats the ey she did it in lecturee 6 but i cant find the reason
# a = lapply(df[c("r_A", "r_B", "r_M")], var)
# b = lapply(df[c("rA_rf", "rB_rf", "rM_rf")], var)
# 
# var_diff = data.frame(Variance=unlist(a),DiffVar=unlist(b))
# var_diff

var1
```

    ##               r_A           r_B          r_M
    ## r_A  2.905690e-03 -0.0003844852 1.171093e-05
    ## r_B -3.844852e-04  0.0105434580 2.646404e-03
    ## r_M  1.171093e-05  0.0026464043 1.615544e-03

``` r
var2
```

    ##               rA_rf         rB_rf        rM_rf
    ## rA_rf  2.905690e-03 -0.0003844852 1.171093e-05
    ## rB_rf -3.844852e-04  0.0105434580 2.646404e-03
    ## rM_rf  1.171093e-05  0.0026464043 1.615544e-03

We can see the tow sample covariances is exactly the same - and that
because *r*<sub>*f*</sub> is constant and we know that
*V**a**r*(*X* + *c*) = *V**a**r*(*X*)

### 3. Scatterplots - SPDR VS. MS

``` r
y.lim <- c(min(df$rA_rf,df$rB_rf), max(df$rA_rf,df$rB_rf))
plot1 <- ggplot(df, aes(x=rM_rf, y=rA_rf)) + geom_point() + ylim(y.lim) +ggtitle("SPDR Gold Shares")
plot2 <- ggplot(df, aes(x=rM_rf, y=rB_rf)) + geom_point() + ylim(y.lim) +ggtitle("Morgan Stanely")
grid.arrange(plot1, plot2, ncol=2) 
```

![](PS2_files/figure-gfm/unnamed-chunk-4-1.png)<!-- -->

The excess returns of SPDR gold shares is less strongly associated with
the market then MS.

This easy to see because when in MS graph we can see that when the
market returns is rising, the MS returns is rising to. On the other
hand, the SPDR is staying basically around 0 (and is not going up with
the market returns). In section 1.2 we got that the covariance of (A,M):
1.1710934^{-5} is lower than (B,M) - 0.0026464 which explaining pretty
well the graph below.

### 4. (a)

consider:
(*r*<sub>*j**t*</sub>−*r*<sub>*f*</sub>) = *α*<sub>*j*</sub> + *β*<sub>*j*</sub>(*r*<sub>*M**t*</sub>−*r*<sub>*f*</sub>) + *ϵ*<sub>*j**t*</sub>
and ill prove that $\\beta\_j = \\frac{cov(r\_j, r\_M)}{var(r\_M)}$

let
*Y* = (*r*<sub>*A*</sub> − *r*<sub>*f*</sub>), *X* = (*r*<sub>*M*</sub> − *r*<sub>*f*</sub>)
I will derive to projection with the projection formula
(*X*<sup>*T*</sup>*X*)<sup> − 1</sup>*X*<sup>*T*</sup>*Y*<sub>*j*</sub>.

``` r
#define our Y, X
Y_1 <- df$rA_rf
Y_2 <- df$rB_rf
X <- df$rM_rf
intercept <- rep(1, length(Y_1)) #making the p=0
X <- cbind(intercept, X)

r_A <- df$r_A
r_B <- df$r_B
r_M <- df$r_M

#projection of X on Y
coeff1 <- solve(t(X) %*% X) %*% t(X) %*% Y_1
reg.beta1 <- round(coeff1[2],3)
cov.r_A <- round(cov(r_A, r_M)/var(r_M),3)


coeff2 <- solve(t(X) %*% X) %*% t(X) %*% Y_2
reg.beta2 <- round(coeff2[2],3)
cov.r_B <- round(cov(r_B, r_M)/var(r_M),3)

cat(paste0("For beta_A: ",cov.r_A, " = ", reg.beta1))
```

    ## For beta_A: 0.007 = 0.007

``` r
cat(paste0("For beta_B: ",cov.r_B, " = ", reg.beta2))
```

    ## For beta_B: 1.638 = 1.638

### (b)

Easy to see that if
*E*(*r*<sub>*j*</sub>) − *r*<sub>*f*</sub> = *β*<sub>*j*</sub>(*E*(*r*<sub>*M*</sub>)−*r*<sub>*f*</sub>)
then
*Y* = (*r*<sub>*A*</sub> − *r*<sub>*f*</sub>), *X* = (*r*<sub>*M*</sub> − *r*<sub>*f*</sub>)
and we know that
*E*(*r*<sub>*j*</sub>) − *r*<sub>*f*</sub> = *β*<sub>*j*</sub>(*E*(*r*<sub>*M*</sub>)−*r*<sub>*f*</sub>) → *E*(*E*(*r*<sub>*j*</sub>) − *r*<sub>*f*</sub>) = *E*(*β*<sub>*j*</sub>(*E*(*r*<sub>*M*</sub>)−*r*<sub>*f*</sub>)) → *E*(*Y*) = *β*<sub>*j*</sub>*E*(*X*)

(*r*<sub>*j**t*</sub>−*r*<sub>*f*</sub>) = *α*<sub>*j*</sub> + *β*<sub>*j*</sub>(*r*<sub>*M**t*</sub>−*r*<sub>*f*</sub>) + *ϵ*<sub>*j**t*</sub> → *E*(*r*<sub>*j**t*</sub>−*r*<sub>*f*</sub>) = *E*(*α*<sub>*j*</sub> + *β*<sub>*j*</sub>(*r*<sub>*M**t*</sub>−*r*<sub>*f*</sub>) + *ϵ*<sub>*j**t*</sub>)

And by the linearity of expectation:
*E*(*r*<sub>*j**t*</sub>) − *r*<sub>*f*</sub> = *E*(*α*<sub>*j*</sub>) + *β*<sub>*j*</sub>(*E*(*r*<sub>*M**t*</sub>) − *r*<sub>*f*</sub>) + *E*(*ϵ*<sub>*j**t*</sub>)

recall that *r*<sub>*f*</sub> is constant, *E*(*ϵ*<sub>*j**t*</sub>) is
0. so if equation (1) holds, which is
*E*(*r*<sub>*j*</sub>) − *r*<sub>*f*</sub> = *β*<sub>*j*</sub>(*E*(*r*<sub>*M*</sub>)−*r*<sub>*f*</sub>)

Than *a*<sub>*j*</sub> = 0

### (c) Estimate equation 2 by OLS regression separately for gold and Morgan Stanley shares using the lm function

``` r
Y_1 <- df$rA_rf
Y_2 <- df$rB_rf
X <- df$rM_rf

r_A <- df$r_A
r_B <- df$r_B
r_M <- df$r_M


reg.model1 <- lm(Y_1~X)
reg.beta1 <- round(reg.model1$coefficients[2],3)
reg.alpha1 <- reg.model1$coefficients[1]

reg.model2 <- lm(Y_2~X)
reg.beta2 <- round(reg.model2$coefficients[2],3)
reg.alpha2 <- reg.model2$coefficients[1]
```

1.  My estimated values *β̂*<sub>*A*</sub>= 0.007 , *β̂*<sub>*B*</sub>=
    1.638
2.  Yes. we can see that the *c**o**v*(*A*, *M*) is smaller than
    *c**o**v*(*A*, *B*). Because we are estimating the beta with single
    linear regression, one of the main impact on the beta is the
    covariance of (X,Y).
3.  Because the beta of SPDR is really low (closer to zero) it means
    that it less correlated with the market the MS (for example, for
    every rise of 1000$ in the market, SPDR will be 7 and MS will be
    1683 .

``` r
y.lim <- c(min(df$rA_rf,df$rB_rf), max(df$rA_rf,df$rB_rf))
plot1 <- ggplot(df, aes(x=rM_rf, y=rA_rf)) + geom_point()+geom_smooth(method = "lm", se = FALSE) + ylim(y.lim) +ggtitle("SPDR Gold Shares")
plot2 <- ggplot(df, aes(x=rM_rf, y=rB_rf)) + geom_point() +geom_smooth(method = "lm", se = FALSE) + ylim(y.lim) +ggtitle("Morgan Stanely")
grid.arrange(plot1, plot2, ncol=2) 
```

    ## `geom_smooth()` using formula 'y ~ x'
    ## `geom_smooth()` using formula 'y ~ x'

![](PS2_files/figure-gfm/unnamed-chunk-7-1.png)<!-- -->

22. To a worried investor i would recommend to invest in the SPDR,
    because it will not be affected alot with the market volatility.

------------------------------------------------------------------------

## Part 2. CAPM -Multiple Hypothesis Testing and the Search for *α*

### 1

``` r
df2 <- read.dta13("data_part2.dta")
df2_len <- length(colnames(df2))

df2[2:df2_len] <- lapply(df2[2:df2_len], function(x) x-0.0041)
```

### 2

``` r
SQPX.reg <- lm(r_swppx ~ r_M, data=df2)
SQPX.alpha <- SQPX.reg$coefficients[1]
SQPX.beta <- SQPX.reg$coefficients[2]
print(paste0("alpha is: ", round(SQPX.alpha,4), " beta is: ",round(SQPX.beta,4)))
```

    ## [1] "alpha is: 0.0018 beta is: 0.9691"

1.  In equation 2, the variance of the residulas may vary by each
    sample. We can use Homoscedasticity if the variance of the residuals
    is distinct, and we cant assume that so Homoscedasticity is not
    plausible.

    In the plot below, for homoscedasticity, we will expect horizontal
    line with equally spread points for a good indication of
    homoscedasticity. This is not the case in our example, where we have
    a heteroscedasticity problem.

    ``` r
    plot(SQPX.reg, which=3)
    ```

    ![](PS2_files/figure-gfm/unnamed-chunk-10-1.png)<!-- -->

2.  Let’s test the null hypothesis that *α* = 0 with 0.1 significant
    level (using lmtest and sandwich libraries):

``` r
#vcovHC for heteroscedastic-robust
SQPX.alpha.hat <- SQPX.alpha
SQPX.alpha.se <- sqrt(vcovHC(SQPX.reg)[1,1])
test.stat <- abs(SQPX.alpha.hat/SQPX.alpha.se)
SQPX.alpha.pval <- 2*(1-pt(test.stat,dim(df2)[1]-1))

#Will we reject the null hypothesis at 0.1 level?
test.stat > qt(0.95, dim(df2)[1]-1)
```

    ## (Intercept) 
    ##        TRUE

``` r
SQPX.alpha.pval <0.1
```

    ## (Intercept) 
    ##        TRUE

So we reject the null hypothesis at 0.1 level that *α* = 0

3.  

``` r
SQPX.CI <- coefci(SQPX.reg, df = dim(df2)[1]-1, level=.90, vcov = vcovHC)[1,]
SQPX.CI 
```

    ##          5 %         95 % 
    ## 0.0005194015 0.0030194328

``` r
#denote that vcovHC for heteroscedastic-robust
```

4.  We can conclude that the alpha will be greater than 0, but not
    significantly - in 90% chances the alpha will be between
    5.1940146^{-4} and 0.0030194 which is nice but not outperform the
    market by alot, but as investor i probably will invest in that fund.

### 3

``` r
# add documntation
Hypo.data <- function(df, fund, r_M, a, fund_name) {
  fund.reg <- lm(fund ~ r_M)
  fund.alpha.hat <- fund.reg$coefficients[1]
  fund.beta.hat <- fund.reg$coefficients[2]
  fund.alpha.se <- sqrt(vcovHC(fund.reg)[1,1])
  
  test.stat <- abs(fund.alpha.hat/fund.alpha.se)
  fund.alpha.pval <- 2*(1-pt(test.stat,dim(df)[1]-1))
  null_status <- test.stat > qt(1-(a/2), dim(df)[1]-1)
  # fund.alpha.pval < a
  
  fund.CI <- coefci(fund.reg, df = dim(df)[1]-1, level=1-a, vcov = vcovHC)[1,]
  ans <- data.frame(fund = fund_name, alpha= round(fund.alpha.hat,4), beta= round(fund.beta.hat,4), nullHypo = null_status, pval = round(fund.alpha.pval,4))
  return(ans)
}
```

``` r
## add documtatton
data <- data.frame(fund = NA, alpha= NA, beta= NA, nullHypo = NA, pval = NA)
for (col in c(1:10)) {
  tmpData <- (Hypo.data(df2,df2[,col+2], df2$r_M, 0.1, names(df2)[3:12][col]))
  data <- rbind(data, tmpData)
} 
rownames(data) <- NULL
data <- na.omit(data)
data
```

    ##       fund   alpha    beta nullHypo   pval
    ## 2  r_fbalx  0.0013  0.6659    FALSE 0.3905
    ## 3  r_fsiix -0.0009  1.0689    FALSE 0.6571
    ## 4  r_swppx  0.0018  0.9691     TRUE 0.0204
    ## 5  r_swssx  0.0021  1.1603    FALSE 0.4730
    ## 6  r_swtsx  0.0020  0.9905     TRUE 0.0105
    ## 7  r_trrbx  0.0005  0.7717    FALSE 0.7259
    ## 8  r_vbisx -0.0019 -0.0101     TRUE 0.0000
    ## 9  r_vbmfx -0.0010 -0.0017    FALSE 0.1850
    ## 10 r_veiex  0.0011  1.2630    FALSE 0.7223
    ## 11 r_vgsix  0.0023  1.2085    FALSE 0.5500

We will reject 3 null hypothesis of there funds: vbisx, swtsx, swppx.

Under the CAPM model, if *α*<sub>*j*</sub> &gt; 0 , the asset has a
higher expected return than than the market. So i can conclude that the
alpha will be the indicator for returns (bigger alpha =&gt; bigger
returns).

The funds that make me suprised whom made to the list for Time’s 50 Best
Mutual Funds in 2018 are those with the negative alpha **AND** with with
rejection of the null hypothesis which is - vbisx.

### 4

1.  Because we have 10 hypothesis testing, and we checking with a
    significance level of 0.1, if only one null hypothesis will be True,
    then we have proportion of $\\frac{1}{10}$ True null hypothesis
    which is 0.1.

2.  If the tow null hypothesis are independent
    *P*(*A* ∩ *B*) = *P*(*A*)*P*(*B*) then we can say that:

$$
P(reject \\ any\\ null) = 1-P(reject \\ no \\ null) \\\\= P(don't \\ reject \\ H\_0^{(1)} \\ \\& \\ \\ don't \\ reject \\ H\_0^{(2)})=1-(1-\\alpha)^2= 
$$

0.19 which is indeed greater than 0.1.

It might be equal to 0.1 if the the tow null hypothesis are fully
dependent: *P*(*A* ∩ *B*) = *m**i**n*(*P*(*a*), *P*(*b*))0.1

3.  According to the proof I showed in the previous section:

$$
P(reject \\ any\\ null) = 1-P(reject \\ no \\ null) \\\\= P(don't \\ reject \\ H\_0^{(1)} \\ \\& \\dots\\& \\ \\ don't \\ reject \\ H\_0^{(n)})=1-(1-\\alpha)^n= 1-(1-0.1)^{10} \\\\ = 0.65
$$

it could be equal to 0.1 by the same explanation in section b.

### 5

``` r
data$bonferon <- data$pval < (0.1/10)
data
```

    ##       fund   alpha    beta nullHypo   pval bonferon
    ## 2  r_fbalx  0.0013  0.6659    FALSE 0.3905    FALSE
    ## 3  r_fsiix -0.0009  1.0689    FALSE 0.6571    FALSE
    ## 4  r_swppx  0.0018  0.9691     TRUE 0.0204    FALSE
    ## 5  r_swssx  0.0021  1.1603    FALSE 0.4730    FALSE
    ## 6  r_swtsx  0.0020  0.9905     TRUE 0.0105    FALSE
    ## 7  r_trrbx  0.0005  0.7717    FALSE 0.7259    FALSE
    ## 8  r_vbisx -0.0019 -0.0101     TRUE 0.0000     TRUE
    ## 9  r_vbmfx -0.0010 -0.0017    FALSE 0.1850    FALSE
    ## 10 r_veiex  0.0011  1.2630    FALSE 0.7223    FALSE
    ## 11 r_vgsix  0.0023  1.2085    FALSE 0.5500    FALSE

We will reject only one null hypothesis (vbisx fund).

### 6

``` r
alpha= 0.1
data <- arrange(data, pval)
data$k <- c(1:nrow(data))
data$RejHypK <- ifelse((alpha/(10+1-data$k))>data$pval, TRUE, FALSE)
#for k+2 the process is ending.
data
```

    ##       fund   alpha    beta nullHypo   pval bonferon  k RejHypK
    ## 1  r_vbisx -0.0019 -0.0101     TRUE 0.0000     TRUE  1    TRUE
    ## 2  r_swtsx  0.0020  0.9905     TRUE 0.0105    FALSE  2    TRUE
    ## 3  r_swppx  0.0018  0.9691     TRUE 0.0204    FALSE  3   FALSE
    ## 4  r_vbmfx -0.0010 -0.0017    FALSE 0.1850    FALSE  4   FALSE
    ## 5  r_fbalx  0.0013  0.6659    FALSE 0.3905    FALSE  5   FALSE
    ## 6  r_swssx  0.0021  1.1603    FALSE 0.4730    FALSE  6   FALSE
    ## 7  r_vgsix  0.0023  1.2085    FALSE 0.5500    FALSE  7   FALSE
    ## 8  r_fsiix -0.0009  1.0689    FALSE 0.6571    FALSE  8   FALSE
    ## 9  r_veiex  0.0011  1.2630    FALSE 0.7223    FALSE  9   FALSE
    ## 10 r_trrbx  0.0005  0.7717    FALSE 0.7259    FALSE 10   FALSE

Ive rejected tow null hypothesis (vbisx, swtsx). Yes i was able to
reject one more null hypothesis then the bonferonni correction.

We can reject more, by using other methods, for example, the FDR by
Benjamini & Hochberg (which are israelis!):

``` r
p <- data$pval
sum(p.adjust(p, "BH")<alpha)
```

    ## [1] 3
