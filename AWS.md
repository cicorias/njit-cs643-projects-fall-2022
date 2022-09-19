# AWS Credential Setup

in IAM on AWS Console create a new IAM policy called `rek-s3-sqs-all` with the following json:

### Policy `rek-s3-sqs-all`
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Action": [
        "sqs:*"
      ],
      "Effect": "Allow",
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "s3:*",
        "s3-object-lambda:*"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "rekognition:*"
      ],
      "Resource": "*"
    }
  ]
}


```

## Add a user and assign policy

Under IAM, Users choose to add a user `cs643user1`

Choose `access key`

Choose `attach existing policies`

Search or find the policy created before: `rek-s3-sqs-all`

Ensure to choose the option button