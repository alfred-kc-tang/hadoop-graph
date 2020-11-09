hadoop jar ./target/q1-1.0.jar alfred.hadoop.graph ./data/graph2.tsv ./data/output2
hadoop fs -getmerge ./data/output2/ output2.tsv
hadoop fs -rm -r ./data/output2
