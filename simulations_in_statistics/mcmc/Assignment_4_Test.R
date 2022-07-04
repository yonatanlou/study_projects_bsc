########################
# Testing Assignment 4 #
########################
rm(list=ls())

# RUN THIS CODE IN THE SAME DIRECTORY WHERE YOUR FILE IS LOCATED
# REPLACE THE NAME TO THE NAME OF YOUR FILE

file_name <- "204767644_Assignment_4.R" 

####################################

# Uploading the required libraries. 
# Make sure to have them installed.
require(stringr)
require("microbenchmark")


pass <- rep(TRUE,3)
names(pass) <- paste("a4q",1:3, sep = "")

# Check that the code does not contain "library" nor "require"
strings_not_allowed <- c("library", "require")
script <- paste(scan(file_name, what = "a"), collapse = "")
if (any(str_detect(script, strings_not_allowed))){
  pass[1:3] <- rep(FALSE,3)
  stop(paste("Your code contains a disallowed string: ",
             paste(strings_not_allowed, collapse = ", ")))
}

source(file_name)
# Q1
# Checks that the code for "a4q1" produces a numeric vector 
# of length equal to the length of x 

x <- rweibull(100,1,2)
f.alpha <- function(x) dgamma(x,1,1)
f.lambda <- function(x) dgamma(x,1,2)
start <- c(2,1)
N <- 100
S <- a4q1(x, f.alpha, f.lambda, start, N)

if(!is.matrix(S) | (nrow(S) != N+1) | (ncol(S) != 2)){
  pass[1] <- FALSE
  stop("a4q1 does not produce a numeric matrix of the required dimensions")
}

if(any(S[1,] != start)){
  pass[1] <- FALSE
  stop("The first row should contain the starting values")
}

# Check running time
x1 <- rweibull(1e3,1,2)
N0 <- 10
assess <- microbenchmark(a4q1(x, f.alpha, f.lambda, start, N0),
                         a4q1(x1, f.alpha, f.lambda, start, N0),
                         a4q1(x, f.alpha, f.lambda, start, N))
med_time <- summary(assess)[,"median"]
if ((med_time[2] > 20*med_time[1]) | (med_time[3] > 20*med_time[1])){
  pass[1] <- FALSE
  stop("The a4q1 function is not efficient enough")
} 

# Checks transition time
n <- sample(1:5,1)
x <- rep(1,n)
f.alpha <- function(a) exp(1/a-a)/a^(n-1)
f.lambda <- function(l) exp(l*(n-1) + 1/l)*l
start <- c(1,1)
N <- 1
f <- function(){
  P <- a4q1(x, f.alpha, f.lambda, start, N)
  P[1,1] == P[2,1]
}
p1 <- mean(replicate(1e5,f()))
p2 <- 1-mean(pmin(rexp(1e5)^(n*rexp(1e5)),1))

if (abs(p1-p2) > 0.01){
  pass[1] <- FALSE
  stop("The distribution of the transition time seems to be wrong")
} 


# Q2
# Checks that the code for "a4q2" produces a numeric vector 
# of length equal to the length of x 

L <- sample(10:20,3)
beta <- runif(1,0.9,1.1)
M <- 1e2
sigma <- runif(1,0.3,0.5)
n <- sample(3:8,1)
X <- array(-1, dim = L)
X[1:n,1:n,n] <- 1
X[1:(n+1),1:(n+1),n+1] <- 1
X[1:(n+2),1:n+2,n+2] <- 1
Y <- X + array(rnorm(prod(L), sd = sigma), dim = L)
E <- a4q2(M, beta, Y, sigma)

if(!is.array(E) | any(dim(E) != dim(Y))){
  pass[2] <- FALSE
  stop("a4q2 does not produce an array of the same dimention as Y")
}

Y1 <- as.matrix(Y[,,1])
E1 <- a4q2(M, beta, Y1, sigma)

if(!is.matrix(E1) | any(dim(E1) != dim(Y1))){
  pass[2] <- FALSE
  stop("a4q2 does not produce a matrix of the same dimention as Y1")
}


# Checks the reconstruction of the image
e0 <- E[n-1,n-1,n-2]
e1 <- E[n-1,n-1,n+1]
e2 <- a4q2(M, 0, 0*Y, sigma)
if (1-e1+e0 + abs(mean(e2) - 0.5) + var(e2) > 0.1){
  pass[2] <- FALSE
  stop("The reconstruction produced by a4q2 is not accurate enough")
} 


# Q3
# Checks that the code for "a4q3" produces a list
# with 2 numeric values of the correct type.
n <- sample(50:100,1)
x <- rnorm(n)
y <- rnorm(n) + x
theta <- runif(3, 1, 2)
param <- c(0,1,100, 100, 2, 5)
n.sweep <- sample(50:100,1)
TH <- a4q3(y, x, theta, param, n.sweep)
if(!is.data.frame(TH) | any(dim(TH) != c(n.sweep+1, 3))){
  pass[3] <- FALSE
  stop("a4q3 does not produce a data frame with the correct dimensions")
}


# Check running time
n1 <- 100*n
x1 <- rnorm(n1)
y1 <- rnorm(n1) + x1
n.sweep1 <- 10*n.sweep
assess <- microbenchmark(a4q3(y, x, theta, param, n.sweep),
                         a4q3(y1, x1, theta, param, n.sweep),
                         a4q3(y, x, theta, param, n.sweep1))
med_time <- summary(assess)[,"median"]
if ((med_time[2] > 10*med_time[1]) | (med_time[3] > 20*med_time[1])){
  pass[3] <- FALSE
  stop("The a4q3 function is not efficient enough")
} 

# Checks the algorithm
n <- 3
x <- y <- rep(0,n)
theta <- runif(3, 1, 1e20)
param <- c(0,1,0, 100, 2, 0.5)
n.sweep <- 1
m <- 1e4
f1 <- function() a4q3(y, x, theta, param, n.sweep)[2,1]
X1 <- replicate(m,f1())
f2 <- function() a4q3(y, x, theta, param, n.sweep)[2,2]/10
X2 <- replicate(m,f2())
f3 <- function(){
  TH <- a4q3(y, x, theta, param, n.sweep)
  (0.5+1.5*TH[2,1]^2)/TH[2,3]
} 
X3 <- replicate(m,f3())
p1 <- ks.test(X1,"pnorm")$p.value
p2 <- ks.test(X2,"pnorm")$p.value
p3 <- ks.test(X3,"pgamma", shape = 3.5)$p.value

if (min(p1,p2,p3) < 0.001){
  pass[3] <- FALSE
  stop("The function a4q3 applies the algorithm incorrectly")
} 


pass

