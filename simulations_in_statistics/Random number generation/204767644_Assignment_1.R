# Assignment 1
# Name: Yonatan Lourie
# I.D. Number:  204767644

# Q1: Replace "return(NA)" by your code
a1q1 <- function(n, j, k, m, seed){
  Y <- rep(NA, n+k)
  
  Y[n+k] = (1103515245*seed+12345)%%2^32
  for (i in seq(n+k-1,n+1,-1)){
    Y[i] <- (1103515245*Y[i+1]+12345)%%2^32 
  }
  
  
  for (i in 1:n){
    
    Y[n+1-i] <- (Y[n+j]+Y[(n+k+1-i)]) %% m
    j = j-1
  }
  
  return(rev(Y[1:n]))
}




# Q2: Replace "return(NA)" by your code
a1q2 <- function(n, shape){
  m <- floor(shape)
  lambda <- m/shape
  c <-(gamma(m)/gamma(shape)*lambda^(-m)*shape^(shape-m)/exp(shape-m))
  out <- matrix(NA, nrow = n, ncol = 2)
  colnames(out) <- c("X", "U")
  a <- gamma(shape)
  b <- gamma(m)
  i=1 
  while(i<=n){
    xn <- sum(rexp(m, rate = lambda))
    U <- runif(1)
    fxn <- xn^(shape-1)*exp(-xn)/a
    gxn <- lambda^m*xn^(m-1)*exp(-lambda*xn)/b
    if(c*gxn*U<=fxn){
      out[i,] <- c(xn, U)
      i = i+1
    }
    
  }
  return(data.frame(out))
}


# Q3: Replace "return(NA)" by your code
a1q3 <- function(n){
  a <- -sqrt(2/exp(1))
  b <- sqrt(2/exp(1))
  out <- matrix(NA, nrow = n, ncol = 3)
  colnames(out) <- c("X", "U1", "U2")
  i=1
  while(i<=n){
    u1 <- runif(1)
    u2 <- runif(1, a, b)
    if(u1^2 <= exp(-0.5*(u2/u1)^2)){
      out[i,] <- c(u2/u1, u1, u2)
      i = i+1
    }
    
  }
  return(data.frame(out))
}
