# Project 1
# Name: Yonatan Lourie
# I.D. Number: 204767644

# Q1: Replace "return(NA)" by your code
ex1q1 <- function(n, prob){
  ans <- c()
  k <- 0
  for (i in 1:n) {
    rnd <- runif(1)
    if (rnd<prob) {
      k=0
      ans = c(ans, k)
    }
    else {
      k = ceiling(log(1-rnd)/log(1-prob)-1)
      ans = c(ans, k)
    }
  }
  
  return(ans)
}


# Q2: Replace "return(NA)" by your code
ex1q2 <- function(n, k, th){
  trans <- (1-(1+th)^(-k))
  U <- runif(n)
  ans <- ((1-U*trans)^(-1/k)-1)
  return(ans)
}
