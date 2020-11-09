hadoop jar ./target/q1-1.0.jar alfred.hadoop.graph ./data/graph1.tsv ./data/output1
hadoop fs -getmerge ./data/output1/ output1.tsv
hadoop fs -rm -r ./data/output1
