########################
# Testing Assignment 2 #
########################
rm(list=ls())

# RUN THIS CODE IN THE SAME DIRECTORY WHERE YOUR FILE IS LOCATED
# REPLACE THE NAME TO THE NAME OF YOUR FILE

file_name <- "204767644_Assignment_2.R" 

####################################

# Uploading the required libraries. 
# Make sure to have them installed.
require(stringr)
require("microbenchmark")
require("MASS")

pass <- rep(TRUE,3)
names(pass) <- paste("a2q",1:3, sep = "")

# Check that the code does not contain "rgeom" nor "rexp"
strings_not_allowed <- c("library", "require",
                         "mvrnorm", "rmvnorm", "rmvn",
                         "arima.sim",
                         "Compound")
script <- paste(scan(file_name, what = "a"), collapse = "")
if (any(str_detect(script, strings_not_allowed))){
  pass[1:3] <- rep(FALSE,3)
  stop(paste("Your code contains a disallowed string: ",
             paste(strings_not_allowed, collapse = ", ")))
}

source(file_name)
# Q1
# Checks that the code for "a1q1" produces a numeric matrix 
# of dimension n times length(mu) 
n <- sample(200:300,1)
m <- sample(20:40,1)
mu <- rnorm(m)
Sigma <- matrix(rnorm(m^2),m,m)
Sigma <- Sigma %*% t(Sigma)
X <- a2q1(n,mu,Sigma)

if(!is.matrix(X) | (!is.numeric(X))){
  pass[1] <- FALSE
  stop("a2q2 does not produce a numeric matrix")
}

if(!all(dim(X) == c(n,m))){
  pass[1] <- FALSE
  stop("The dimentions of the output are wrong")
}

# Check running time
n <- 10^4
m <- 10
mu <- rnorm(m)
Sigma <- matrix(rnorm(m^2),m,m)
Sigma <- Sigma %*% t(Sigma)
asses <- microbenchmark(a2q1(n,mu,Sigma),
               mvrnorm(n, mu, Sigma))
med_time <- summary(asses)[,"median"]
if (med_time[1] > 10*med_time[2]){
  pass[1] <- FALSE
  stop("The a2q2 function is not efficient enough")
} 

# Checks the distribution of the rows
X <- a2q1(n,mu,Sigma)
a <- runif(m)
a_X <- as.vector(X %*% a)
a_mean <- sum(a*mu)
a_sd <- as.vector(sqrt(a %*% Sigma %*% a)) 
KS <- ks.test(a_X, "pnorm", mean = a_mean, sd = a_sd)
if (KS$p.value < 10^(-4)){
  pass[1] <- FALSE
  stop("The distribution produced by a2q1 is not multinormal")
} 

# Checks the distribution of the columns
p_val <- rep(0,m)
for (j in seq(m)){
  KS <- ks.test(X[,j], "pnorm", mean = mu[j], sd = sqrt(Sigma[j,j]))
  p_val[j] <- KS$p.value
}

if (min(p_val) < 10^(-4)){
  pass[1] <- FALSE
  stop("The marginal distributions produced by a2q1 are wrong")
} 



# Q2
# Checks that the code for "a2q2" produces a numeric matrix 
# of dimension n times m 
n <- sample(200:300,1)
m <- sample(20:40,1)
a <- runif(1, min = -0.8, max = 0.8)
b <- runif(1, min = -0.8, max = 0.8)
sd <- runif(1, min = 0, max = 5)
sd.0 <- runif(1, min = 0, max = 5)
X <- a2q2(n, m, a, b, sd, sd.0)

if(!is.matrix(X) | (!is.numeric(X))){
  pass[2] <- FALSE
  stop("a2q2 does not produce a numeric matrix")
}

if(!all(dim(X) == c(n,m))){
  pass[2] <- FALSE
  stop("The dimentions of the output are wrong")
}

# Check running time
n0 <- 10^2
n1 <- 10^4
m0 <- 10^1
m1 <- 10^3
a <- runif(1, min = -0.8, max = 0.8)
b <- runif(1, min = -0.8, max = 0.8)
sd <- runif(1, min = 0, max = 5)
sd.0 <- runif(1, min = 0, max = 5)
asses <- microbenchmark(a2q2(n0, m0, a, b, sd, sd.0),
                        a2q2(n1, m0, a, b, sd, sd.0),
                        a2q2(n0, m1, a, b, sd, sd.0))
med_time <- summary(asses)[,"median"]
if (any(c(med_time[1] > 10^3, med_time[2:3]/med_time[1] > 300))){
  pass[2] <- FALSE
  stop("The a2q2 function is not efficient enough")
} 

# Checks the marginal distributions
n <- 10^6
m <- 10
a <- runif(1, min = -0.8, max = 0.8)
b <- runif(1, min = -0.8, max = 0.8)
s <- runif(1, min = 0, max = 5)
s.0 <- s*sqrt(1+(a+b)^2/(1-a^2))
X <- a2q2(n, m, a, b, s, s.0)
KS <- ks.test(X[,sample(5:10,1)], "pnorm", sd = s.0)
if (KS$p.value < 10^(-4)){
  pass[2] <- FALSE
  stop("The marginal distribution in a2q2 is wrong")
} 

# Check autocorrelation
V <- var(X)
v <- rep(mean(diag(V)),3)
gam <- rep(s.0^2,3)
for (j in 1:2){
  v[j+1] <- mean(diag(V[,-(1:j)]))
  gam[j+1] <- s^2*((a+b)+a*(a+b)^2/(1-a^2))*a^(j-1)
}  
if (max(abs(v/gam - 1)) > 0.05) {
  pass[2] <- FALSE
  stop("The correlation structure is wrong")
} 


# Q3
# Checks that the code for "a2q3" produces a numeric 
# vector of the correct length
n <- sample(500:1000,1)
rN <- function(n) rpois(n, lambda = 20)
rX <- function(n) rexp(n)
S <- a2q3(n, rN, rX)
if(!all(is.vector(S),is.numeric(S),length(S) == n)){
  pass[3] <- FALSE
  stop("a3q2 does not produce a numeric vector of the correct length")
}


# Check running time
n0 <- 10^2
n1 <- 10^4
asses <- microbenchmark(a2q3(n0, rN, rX),
                        a2q3(n1, rN, rX))
med_time <- summary(asses)[,"median"]
if (any(c(med_time[1] > 2*10^3, med_time[2]/med_time[1] > 300))){
  pass[3] <- FALSE
  stop("The a2q3 function is not efficient enough")
} 

# Checks the marginal distributions
n <- 10^4
size <- sample(2:6,1)
prob <- runif(1, min = 0.3, max = 0.7)
rN <- function(n) rbinom(n, size = size, prob = prob)
rX <- function(n) rexp(n)
S <- a2q3(n, rN, rX)
CDF <- function(x) {
  p <- dbinom(seq(size),size,prob)
  D <- 0
  for(i in seq(size)) D <- D + p[i]*pgamma(x,i)
  return(D/sum(p))
}
KS <- ks.test(S[S > 0], CDF)
if (KS$p.value < 10^(-4)){
  pass[3] <- FALSE
  stop("The marginal distribution in a2q3 is wrong")
} 

pass

