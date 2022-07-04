########################
# Testing Assignment 3 #
########################
rm(list=ls())

# RUN THIS CODE IN THE SAME DIRECTORY WHERE YOUR FILE IS LOCATED
# REPLACE THE NAME TO THE NAME OF YOUR FILE

file_name <- "204767644_Assignment_3.R" 
setwd(dirname(rstudioapi::getActiveDocumentContext()$path))

####################################

# Uploading the required libraries. 
# Make sure to have them installed.
require(stringr)
require("microbenchmark")
require("MASS")

pass <- rep(TRUE,3)
names(pass) <- paste("a3q",1:3, sep = "")

# Check that the code does not contain "rgeom" nor "rexp"
strings_not_allowed <- c("library", "require",
                         "boot")
script <- paste(scan(file_name, what = "a"), collapse = "")
if (any(str_detect(script, strings_not_allowed))){
  pass[1:3] <- rep(FALSE,3)
  stop(paste("Your code contains a disallowed string: ",
             paste(strings_not_allowed, collapse = ", ")))
}

source(file_name)
# Q1
# Checks that the code for "a3q1" produces a numeric vector 
# of length equal to the length of x 

n <- sample(30:40,1)
x <- sample(20:30,sample(3:7,1))
p0 <- runif(1,0.1,0.3)
p1 <- runif(1,0.5,0.6)
N <- 1e3
S <- a3q1(x, n, p0, p1, N)

if(!is.numeric(S) | (length(x) != length(S))){
  pass[1] <- FALSE
  stop("a3q1 does not produce a numeric vector of the same length as x")
}

# Check running time
N1 <- 1e3
N2 <- 1e4
x <- 20:29
asses <- microbenchmark(a3q1(x[1], n, p0, p1, N1),
                        a3q1(x, n, p0, p1, N1),
                        a3q1(x[1], n, p0, p1, N2))
med_time <- summary(asses)[,"median"]
if ((med_time[2] > 2*med_time[1]) | (med_time[3] > 20*med_time[1])){
  pass[1] <- FALSE
  stop("The a3q1 function is not efficient enough")
} 

# Checks the accuracy of the approximation
n <- sample(30:40,1)
x <- sample((n-10):(n-5),1)
p0 <- runif(1,0.3,0.4)
p1 <- x/n
S <- a3q1(x, n, p0, p1, 1e6)
S.R <- 1-pbinom(x,n,p0)
if (abs(S/S.R - 1) > 0.1){
  pass[1] <- FALSE
  stop("The approximation produced by a3q1 is not accurate enough")
} 


# Checks the distribution of the approximation
N <- 1e2
S <- replicate(1e5,a3q1(x, n, p0, p1, N))
S.R <- 1-pbinom(x,n,p0)
y <- (x+1):n
V.R <- sum(dbinom(y,n,p0)^2/dbinom(y,n,p1))-S.R^2
V <- var(S)*N
if (abs(V/V.R - 1) > 0.1){
  pass[1] <- FALSE
  stop("The distribution of the approximation produced by a3q1 is wrong")
} 


# Q2
# Checks that the code for "a3q2" produces a numeric vector 
# of length equal to the length of x 

f <- function(u) exp(-u)/(1 + u^2)
g <- function(u) exp(-0.5)/(1 + u^2)
m <- exp(-0.5)*pi/4
N <- 1e4
I <- a3q2(f, runif, g, m, N)
if(!is.numeric(I) | (length(I) != 1)){
  pass[2] <- FALSE
  stop("a3q2 does not produce a numeric vector of the length 1")
}

# Check running time
N1 <- 1e3
N2 <- 1e4
asses <- microbenchmark(a3q2(f, runif, g, m, N1),
                        a3q2(f, runif, g, m, N2))
med_time <- summary(asses)[,"median"]
if ((med_time[1] > 500) | (med_time[2] > 15*med_time[1])){
  pass[2] <- FALSE
  stop("The a3q2 function is not efficient enough")
} 

# Checks the accuracy of the approximation
a <- runif(1,0.5,2)
b <- runif(1,1.5,2.5)
f <- function(u) exp(-a*u)/(1 + u^b)
g <- function(u) exp(-a*0.5)/(1 + u^b)
m <- integrate(g,0,1)$value
N <- 1e4
I <- a3q2(f, runif, g, m, N)
I.R <- integrate(f,0,1)$value
if (abs(I/I.R - 1) > 0.01){
  pass[2] <- FALSE
  stop("The approximation produced by a3q2 is not accurate enough")
} 


# Checks the distribution of the approximation
r <- runif(1,0.3,0.5)
f <- function(u) u
g <- function(u) r*u + sqrt(1-r^2)*rnorm(length(u))
m <- 0
N <- 1e4
S <- replicate(1e4,a3q2(f, rnorm, g, m, N))
V <- var(S)*N
V.R <- 1-r^2
if (abs(V/V.R - 1) > 0.1){
  pass[2] <- FALSE
  stop("The distribution of the approximation produced by a3q2 is wrong")
} 



# Q3
# Checks that the code for "a3q3" produces a list
# with 2 numeric values of the correct type.
X <- rnorm(1e2)
m <- 5
B <- 1e4
out <- a3q3(X, m, B)
out.names <- c("p.value", "statistic")
if(!is.list(out) | any(sort(names(out)) != out.names)){
  pass[3] <- FALSE
  stop("a3q3 does not produce a list with the correct names")
}

out <- simplify2array(out) 
if(!is.numeric(out) | (length(out) != 2)){
  pass[3] <- FALSE
  stop("a3q3 does not produce a list with 2 numeric values")
}

# Check running time
X1 <- rnorm(1e2)
m0 <- 1
m1 <- 5
X2 <- rnorm(1e3)
m2 <- 50
B <- 1e3
asses <- microbenchmark(a3q3(X1,m0,B),
                        a3q3(X1,m1,B),
                        a3q3(X2,m2,B))
med_time <- summary(asses)[,"median"]
if ((med_time[2] > 2*med_time[1]) | (med_time[3] > 20*med_time[1])){
  pass[3] <- FALSE
  stop("The a3q3 function is not efficient enough")
} 

# Checks the accuracy of the approximation
X <- rnorm(1e2)
m <- 3
B <- 1e2
out <- a3q3(X, m, B)
index <- 1:98
d <- out$statistic - max(X[index]+X[index+1]+X[index+2])
if (abs(d) > 1e-10){
  pass[3] <- FALSE
  stop("The function a3q3 computes the statistic incorrectly")
} 
m <- 1
B <- 1e5
out <- a3q3(X, m, B)
M <- max(X)
mu <- mean(X)
sig <- sd(X)
p <- pnorm(out$statistic, mean = mu, sd = sig)^100
if (abs(out$p.value/(1-p) - 1) > 0.05){
  pass[3] <- FALSE
  stop("The function a3q3 computes the p-value incorrectly")
} 


# Checks the distribution of the approximation
X <- rnorm(1e2)
m <- 5
out <- a3q3(X, m, 1e5)
P <- replicate(1e4,a3q3(X, m, 10)$p.value)
V <- var(P)*10
V.R <- out$p.value*(1-out$p.value)
if (abs(V/V.R - 1) > 0.1){
  pass[3] <- FALSE
  stop("The distribution of the approximate p.value produced by a3q3 is wrong")
} 

pass

