# Project 4
# Name: 
# I.D. Number:

# Q1: Replace "return(NA)" by your code
ex4q1 <- function(n_sim, n_conv, a, b){
  X_sim <- Y_sim <- rep(NA, n_sim)
  X <- rnorm(1, mean = b[1]/a[1], sd = sqrt(1/a[1]))
  for (i in seq(n_sim)) {
    X_sim[i] <- X
    Y_sim[i] <- rnorm(1, mean = b[2]/(X^2+a[2]), sd = 1/sqrt(X^2+a[2]))
    X <- rnorm(1, mean = b[1]/(Y_sim[i]^2+a[1]), sd = 1/sqrt(Y_sim[i]^2+a[1]))
  }
  return(cbind(X_sim,Y_sim))
}



# Q2: Replace "return(NA)" bY Your code
ex4q2 <- function(data, mu, tau, r, lam, n_sim, n_conv){
  a <- 0
  s<-sum(data^2)
  R_Gamma <- rgamma(1,r+length(data)/2,lam+s*0.5)
  for(i in seq(n_conv)){
    stemp<-sum(data)
    ltemp<-length(data)
    a <- rnorm(1,mean=(mu/tau^2+stemp*R_Gamma)/(1/tau^2+ltemp*R_Gamma),sd=sqrt(1/(1/tau^2+ltemp*R_Gamma)))
    R_Gamma <- rgamma(1,r+ltemp/2,lam+0.5*sum((data-a)^2))
  }
  sa<- rep(NA,n_sim)
  srgam <- rep(NA,n_sim)
  a <- rnorm(1,mean=(mu/tau^2+stemp*R_Gamma)/(1/tau^2+ltemp*R_Gamma),sd=sqrt(1/(1/tau^2+ltemp*R_Gamma)))
  for(i in seq(n_sim)){
    sa[i] <- a
    srgam[i] <- rgamma(1,r+ltemp/2,lam+0.5*sum((data-a)^2))
    a <- rnorm(1,mean=(mu/tau^2+srgam[i]*stemp)/(1/tau^2+srgam[i]*ltemp),sd=sqrt(1/(1/tau^2+srgam[i]*ltemp)))
  }
  df <- data.frame(a=sa,rgam=srgam)
  return(as.matrix(df))
}