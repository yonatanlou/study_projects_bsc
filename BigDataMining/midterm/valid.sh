{
    read -r line
    line=`tr ',' ' ' <<< $line`

    echo "$line"

    while IFS=, read
done
} < stackoverflow_numpy_qa.csv
