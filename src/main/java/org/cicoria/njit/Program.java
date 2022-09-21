package org.cicoria.njit;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Program {
    private static Logger logger = LoggerFactory.getLogger(Program.class);

    @Parameter(names = {"--processor", "-p"},required = true, validateWith = Arguments.class, description = "must be either 'initiator' or 'worker'")
    private String processor;

    @Parameter(names = {"--region", "-r"}, description = "AWS region - default is us-east-1")
    private String region = "us-east-1";

    @Parameter(names = {"--s3bucket", "-b"}, description = "S3 bucket name - default is njit-cs-643")
    private String bucket = "njit-cs-643";

    @Parameter(names = {"--label", "-l"}, description = "label to recognize - default is car")
    private String label = "car";

    @Parameter(names = {"--queue", "-q"}, description = "SQS queue name - default is sharedqueue")
    private String queue = "sharedqueue";

    @Parameter(names = "--help", help = true)
    private boolean help;

    public static void main(String ... argv) {

        try {
            Program main = new Program();
            JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(argv);
            main.run();
        } catch (ParameterException e) {
            System.err.println(e.getMessage());
            e.usage();
            System.exit(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(2);
        }

    }

    private void run() throws InterruptedException {
        System.out.println("Running with processor: " + processor);

        if (processor.equalsIgnoreCase("initiator")) {
            Initiator initiator = new Initiator(bucket, region, label, queue);
            initiator.run();
        } else {
            Worker worker = new Worker(region, bucket, queue);
            worker.run();
        }


        System.out.println("Hello worddld!");
    }

}