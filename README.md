
# Setting up a Real-Time Transcription business server

Real-Time Transcription (RTT) takes the audio content of a host's media stream and transcribes it into written words in real time. This Java project demonstrates how to set up a business server that your client uses to start and stop Real-Time Transcription tasks.

## Understand the tech

To start transcribing the audio in a channel in real-time, you send an `HTTP` request to the Agora SD-RTN™ through your business server. Real-Time Transcription provides the following modes:

* Transcribe speech in real-time, then stream this data to the channel.
* Transcribe speech in real-time, store the text in the `WebVTT` format, and upload the file to third-party cloud storage.

Real-Time Transcription transcribes at most three speakers in a channel. When there are more than three speakers, the top three are selected based on volume, and their audio is transcribed.

The following figure shows the workflow to start, query, and stop a Real-Time Transcription task:

![Real-Time Transcription business server](https://docs-git-milestone37-speech-to-text-Agora-gdxe.vercel.app/en/assets/images/real-time-transcription-server-07d073102bebc8cf0f41e6985efc56b1.svg)

In order to use the RESTful API to transcribe speech, your server make the following calls:

1. `acquire`: Request a `builderToken` that authenticates the user and gives permission to start Real-Time Transcription . You must call `start` using this `builderToken` within five minutes.
1. `start`: Begin the transcription task. Once you start a task, `builderToken` remains valid for the entire session. Use the same `builderToken` to query and stop the task.
1. `query`: Check the task status.
1. `stop`: Stop the transcription task.

## Prerequisites

In order to set up a demo Real-Time Transcription business server, you must have:

* Java Development Kit (JDK) 1.8 or higher.
* Installed [git](https://git-scm.com/downloads) on your development machine.
* Installed [Maven](https://maven.apache.org/download.cgi) on your system.
* An Integrated Development Environment (IDE) configured to work with Maven Java projects. 
* [Curl](https://curl.se/download.html) for testing your server implementation.
* Enabled Real-Time Transcription for your project. Contact sales@Agora.io
* Activated a [supported cloud storage service](#supported-third-party-cloud-storage-services) to record and store Real-Time Transcription texts.

## Implement a business server

You create a business server as a bridge between your app and Agora Real-Time Transcription.
Implementing a business server to manage Real-Time Transcription provides the following benefits:

* Improved security as your `apiKey`, `apiSecret`, `builderToken`, and `taskId`, are not exposed to the client.
* Token processing is securely handled on the business server.
* Avoid splicing complex request body strings on the client side to reduce the probability of errors.
* Implement additional functionality on the business server. For example, billing for  Real-Time Transcription use, checking
user privileges and payment status of a user.
* If the REST API is updated, you do not need to update the client.

### Build and run the demo business server

To set up the demo Real-time Transcription business server, take the following steps:

1. **Download the Git repository**

    Clone the `agora-rtt-server` repository to your development device using the following command:

    ```bash
    git clone https://github.com/saudsami/agora-rtt-server <your download directory>
    ```

1. **Specify connection variables**

    Open the `agora-rtt-server\src\main\java\rtt\RttTask.java` file and update the values for `appId`, `appCertificate`, `customerId`, and `customerSecret` from [Agora Console](https://console.agora.io/). Also specify the `ossAccessKey`, `ossSecretKey`, and the `ossBucketName` to configure cloud storage.

1. **Build the server project**

    Execute the following command in the terminal to download the dependencies and initiate the Maven build process:

    ```bash
    mvn clean install
    ```

1. **Start the server**

    In your IDE, **Start debugging** to launch the server. You see the following message in the terminal:

    ```
    RTT server started on port 80
    ```

## Test your implementation

To test your business server, take the following steps:

1. To start an RTT task, execute the following command in a terminal window:

    ```bash
    curl -X POST -H "Content-Type: application/json" -d "{\"UserId\": \"123\", \"channelName\": \"demo\"}" http://localhost:80/rttStart
    ```

    The command makes an HTTP request to your RTT server with a JSON body. The body contains the following parameters:
    * `UserId`: The parameter identifies the user starting the RTT task, so that the business server may check user's privileges and payment status.
    * `channelName`: The channel for which the RTT task is to be started.

    You see a message in the terminal confirming that the RTT task was started successfully. You also see a confirmation message displayed in the server terminal with the channel name and task ID.

    
1. To query the status of an RTT task, execute the following command in a terminal window:

    ```bash
    curl -X POST -H "Content-Type: application/json" -d "{\"channelName\": \"demo\"}" http://localhost:80/rttQuery
    ```
    Your server retrieves the task ID and builder token for the task associated with the `channelName` specified in the request and sends a request to query the task status. You see the retrieved status displayed in the terminal.

1. To stop an RTT task, execute the following command in a terminal window:

    ```bash
    curl -X POST -H "Content-Type: application/json" -d "{\"channelName\": \"demo\"}" http://localhost:80/rttStop
    ```

     Your server retrieves the task ID and builder token for the task associated with the `channelName` specified in the request and sends a request to stop the task. You see a confirmation message displayed in the terminal.


## Reference

This section contains additional content that completes the information in this page, or points you to documentation that explains other aspects to this product.

### REST API

To test the Real-time Transcription REST API, and to see request parameter details, refer to the [Postman Collection](https://documenter.getpostman.com/view/6319646/SVSLr9AM#69bd200a-7543-4104-8ccc-415741abbeb7). 

### List of supported languages

Use the following language codes in the `recognizeConfig.language` parameter of the start request. The current version supports at most two languages, separated by commas. 

| Language                         | Code  | 
| -------------------------------- | ----- |
| Chinese (Cantonese, Traditional) | zh-HK |
| Chinese (Mandarin, Simplified)   | zh-CN |
| Chinese (Taiwanese Putonghua)    | zh-TW |
| English (India)                  | en-IN |
| English (US)                     | en-US |
| French (French)                  | fr-FR |
| German (Germany)                 | de-DE |
| Hindi (India)                    | hi-IN |
| Indonesian (Indonesia)           | id-ID |
| Italian (Italy)                  | it-IT |
| Japanese (Japan)                 | ja-JP |
| Korean (South Korea)             | ko-KR |
| Portuguese (Portugal)            | pt-PT |
| Spanish (Spain)                  | es-ES |

### Supported third-party cloud storage services

The following third-party cloud storage service providers are supported:

* [Alibaba Cloud](https://www.alibabacloud.com/product/oss)
* [Amazon S3](https://aws.amazon.com/s3/?nc1=h_ls)
* [Baidu AI Cloud](https://intl.cloud.baidu.com/product/bos.html)
* [Google Cloud](https://cloud.google.com/storage)
* [Huawei Cloud](https://www.huaweicloud.com/intl/en-us/product/obs.html)
* [Kingsoft Cloud](https://en.ksyun.com/nv/product/KS3.html)
* [Microsoft Azure](https://azure.microsoft.com/en-us/services/storage/blobs/)
* [Qiniu Cloud](https://www.qiniu.com/en/products/kodo)
* [Tencent Cloud](https://intl.cloud.tencent.com/product/cos)

