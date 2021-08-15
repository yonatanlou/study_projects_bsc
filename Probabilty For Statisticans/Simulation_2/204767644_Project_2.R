# Project 2
# Name: Yonatan Lourie
# I.D. Number: 204767644

# Q1: Replace "return(NA)" by your code
ex2q1 <- function(n){
  AB <- matrix(NA,n,2)
  colnames(AB) <- c("A","B")
  for (i in seq(n)){
    flag <- FALSE
    while (!flag) {
      try1 <- runif(2,0,1)
      U <- runif(1)
      if(sum(try1^2)<1){
        flag <- (U <= (sqrt(1-sum(try1^2))))
      }
    }
    AB[i,] <- try1
  }
  return(data.frame(AB))
}
# Q2: Replace "return(NA)" by your code
ex2q2 <- function(alpha, n.copy){
  x  <-  sapply(1:n.copy, function(id){
    density <- runif(length(alpha)) 
    y <- mean(sin(sum(density*alpha)))})
  error <- sd(x)/length(alpha)
  return(list("value"=mean(x),"error"= error))
}
