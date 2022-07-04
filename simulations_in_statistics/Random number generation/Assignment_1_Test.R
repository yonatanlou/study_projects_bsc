########################
# Testing Assignment 1 #
########################
rm(list=ls())

# RUN THIS CODE IN THE SAME DIRECTORY WHERE YOUR FILE IS LOCATED
# REPLACE THE NAME TO THE NAME OF YOUR FILE

file_name <- "204767644_Assignment_1.R" 

####################################

pass <- TRUE

# Check that the code does not contain "rgeom" nor "rexp"
require(stringr)
strings_not_allowed <- c("library", "require", 
                         "rgamma", "rnorm")
script <- paste(scan(file_name, what = "a"), collapse = "")
if (any(str_detect(script, strings_not_allowed))){
  pass <- FALSE
  stop(paste("Your code containes a disallowed function: ",
             paste(strings_not_allowed, collapse = ", ")))
}

source(file_name)
# Q1
# Checks that the code for "a1q1" produces a numeric vector 
# of length n
n <- sample(200:300,1)
j <- 36
k <- 99
m <- 2^(30)
seed <- 2022
X <- a1q1(n,j,k,m,seed)
if((length(X) != n) | (!is.numeric(X))){
  stop("a1q2 does not produce a vector of length n of integers")
}

# Checks the updating formula
xk <- X[1:10]
xj <- X[1:10 + k-j]
xn <- X[1:10 + k]
if (!all(xn == ((xk+xj) %% m))){
  pass <- FALSE
  stop("The updating formula in ex1q1 is incorrect")
} 

# Checks initiation
xn <- X[1:5]
ref <- c(889348903, 593787392, 183783936, 117656064, 955441728)
if (!all(xn == ref)){
  pass <- FALSE
  stop("The initialization in ex1q1 is incorrect")
}

# Check running time
n <- 10^5
t0 <- Sys.time(); out <- a1q1(n,j,k,m,seed); t1 <- Sys.time()
if (t1-t0 > 0.2){
  pass <- FALSE
  stop("The a1q2 function is not efficient enough")
} 

# Q2
# Checks that the code for "a1q2" produces a data frame 
# of the correct dimensions
n <- sample(500:1000,1)
shape <- runif(1, 2.5, 6.5)
out <- a1q2(n,shape)
if(!is.data.frame(out) | !all(dim(out) == c(n,2))){
  pass <- FALSE
  stop("a1q2 does not produce a data frame of the correct dimension")
}
if(!all(sort(names(out)) == c("U","X")))
{
  pass <- FALSE
  stop("a1q2 does not assign correct names to the variables")
}

# Check running time
n <- 10^5
t0 <- Sys.time(); out <- a1q2(n,shape); t1 <- Sys.time()
if (t1-t0 > 10){
  pass <- FALSE
  stop("The a1q2 function is not efficient enough")
} 

# Checks the marginal distributions
KS <- ks.test(out$X, "pgamma", shape)
if (KS$p.value < 10^(-4)){
  pass <- FALSE
  stop("The marginal distribution in a1q2 is wrong")
} 

if (max(out$U) < 0.99) {
  pass <- FALSE
  stop("The rejection frequency a1q2 is not optimal")
} 


# Q3
# Checks that the code for "a1q3" produces a data frame 
# of the correct dimensions
n <- sample(500:1000,1)
out <- a1q3(n)
if(!is.data.frame(out) | !all(dim(out) == c(n,3))){
  pass <- FALSE
  stop("a1q2 does not produce a data frame of the correct dimension")
}
if(!all(sort(names(out)) == c("U1","U2","X"))){
  pass <- FALSE
  stop("a1q2 does not assign correct names to the variables")
}

# Check running time
n <- 10^5
t0 <- Sys.time(); out <- a1q3(n); t1 <- Sys.time()
if (t1-t0 > 10){
  pass <- FALSE
  stop("The a1q3 function is not efficient enough")
} 

# Checks the marginal distributions
KS <- ks.test(out$X, "pnorm")
if (KS$p.value < 10^(-4)){
  pass <- FALSE
  stop("The marginal distribution in a1q3 is wrong")
} 

equal <- with(out, all(abs(X - U2/U1) < 10^(-6)))
if (!equal){
  pass <- FALSE
  stop("The variable X in a1q3 is not equal to U2/U1")
} 

pass
