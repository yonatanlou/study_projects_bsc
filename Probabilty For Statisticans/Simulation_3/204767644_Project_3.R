# Project 3
# Name: Yonatan Lourie
# I.D. Number: 204767644

# Q1: Replace "return(NA)" by your code
suppressWarnings(library(comprehenr))
ex3q1 <- function(n, mu, sig, r){
  sigma <- to_vec(for (x in seq(length(mu))) for (y in seq(length(mu))) (r^abs(x-y))*(sig^2))
  sigma.root <- matrix(sigma, length(mu), length(mu))
  sig_eigen<- eigen(sigma.root,symmetric=T)
  U <- sig_eigen$vectors
  lambda <- diag(sqrt(sig_eigen$values))
  sigma.root.eigen <- U %*% lambda %*% t(U)
  X <- matrix(rnorm(n*length(mu)),length(mu),n)
  Y <- sigma.root.eigen %*% X
  Y <- sweep(Y,1,mu,"+")
  return(t(Y))
}

# Q2: Replace "return(NA)" by your code
ex3q2 <- function(data, statistic, R){
  t <- rep(NA, R)
  t0 <- statistic(data)
  for (i in c(1:R)){
    mu_observed <- colMeans(data)
    sig_observed <- var(data)
    Y_sim <- mvrnorm(10^2,mu_observed,sig_observed)
    t[i] <-  statistic(Y_sim)
  }
  return(list("t0"=t0, "t"=t))
}
