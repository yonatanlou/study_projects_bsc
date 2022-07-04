# Assignment 2
# Name: 
# I.D. Number:

# Q1: Replace "return(NA)" by your code
multivnorm=function(mu,A){
  r = length(mu)
  Z = rnorm(r)
  return(A %*% Z + mu)
}

a2q1 <- function(n, mu, Sigma){
  res_mat =  matrix(nrow=n, ncol=length(mu))
  A = t(chol(Sigma))
  for (i in 1:n) {
    res_mat[i,] = multivnorm(mu, A)
  }
  return(res_mat)
}


# Q2: Replace "return(NA)" by your code
a2q2 <- function(n, m, a, b, sd, sd.0){
  res_mat = matrix(nrow=n, ncol=m)
  for (i in 1:n) {
    e = rnorm(m, mean=0, sd=sd)
    y = numeric(length = m)
    y0 = rnorm(1, mean = 0, sd = sd.0)
    y[1] = a*y0 + b*rnorm(1, mean = 0, sd=sd) + e[1]
    for (j in 2:m) {
      y[j] = a*y[j-1] + b*e[j-1] + e[j]
    }
    res_mat[i,] = y
  }
  return(res_mat)
}
# Q3: Replace "return(NA)" by your code
a2q3 <- function(n, rN, rX){
  res = numeric(length = n)
  m = rN(n)
  for(i in seq(n)){
    res[i] = sum(rX(m[i]))
  }
  return(res)
}