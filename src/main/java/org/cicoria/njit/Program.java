package org.cicoria.njit;

public class Program {
    public static void main(String[] args) {

        var bucket = "test-bucket-njit";
        var region = "us-east-1";

        Initiator initiator = new Initiator();

        initiator.Run(bucket, region, "car");

        System.out.println("Hello world!");
    }
}