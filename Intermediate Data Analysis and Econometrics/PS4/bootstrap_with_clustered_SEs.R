###########################################
# Bootstrap with clustered standard errors
###########################################

# Set B
B <- 1000

# Create a vector with schools IDs.
schools <- unique(dftr$schoolid)

# Create a matrix to store our estimates
mat <- matrix(NA, nrow = B, ncol = 8)

# Loop over bootstrap repetitions
for (b in 1:B) {
  
  # Randomly choose schools
  i <- sample(1:length(schools), length(schools), replace = TRUE)
  
  # Which school are included?
  inc_school <- schools[i]
  
  # Collect data only for the included schools
  for(j in 1:length(schools)) {
    if (j == 1) {
      # Select the school
      df_boot <- dftr[which(dftr$schoolid == inc_school[j]), ]
      
      # Create a new school index (because we treat each bootstraped 
      # school as a different school)
      df_boot$new <- j
      
    } else {
      # Select the school
      temp <- dftr[which(dftr$schoolid == inc_school[j]), ]
      
      # Create a new school index (because we treat each bootstraped
      # school as a different school)
      temp$new <- j
      
      # Vertically merge dataframes
      df_boot <- rbind(df_boot, temp)
    }
  }
  
  # Run the desired model using the bootstrapped dataset
  model <- lm(
    stdR_totalscore ~ bottomhalf + percentile + percentilesq +
      percentilecub + etpteacher + girl + agetest,
    data = df_boot
  )
  
  # Store the desired coefficiient
  mat[b, ] <- model$coefficients
}

# Report the results
c5 <- coeftest(t5pAc1, df = Inf, vcov. = cov(mat))