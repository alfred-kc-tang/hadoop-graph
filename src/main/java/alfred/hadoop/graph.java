package alfred.hadoop;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Q1 {

  public static class EmailMapper extends Mapper<LongWritable, Text, Text, Text> {

    private Text mkey = new Text();
    private Text mval = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
      String[] token = value.toString().split("\t");
      mkey.set(token[0]);
      mval.set(token[1] + "," + token[2]);
      context.write(mkey, mval);
    }
  }

  public static class MaxReducer extends Reducer<Text, Text, Text, Text> {

    private Text rval = new Text();

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
      int maxW = 0;
      int maxID = 0;
      for (Text val : values) {
        String[] val_token = val.toString().split(",");
        int id = Integer.parseInt(val_token[0]);
        int weight = Integer.parseInt(val_token[1]);
        if (maxW < weight) {
          maxW = weight;
          maxID = id;
        } else if (maxW == weight) {
          if (maxID > id) {
            maxID = id;
          }
        }
      }
      rval.set(maxID + "," + maxW);
      context.write(key, rval);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q1");
    job.setJarByClass(Q1.class);
    job.setMapperClass(EmailMapper.class);
    job.setCombinerClass(MaxReducer.class);
    job.setReducerClass(MaxReducer.class);
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(Text.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(Text.class);
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
