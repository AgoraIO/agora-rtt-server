
# Setting up a Real-Time Transcription business server

Real-Time Transcription takes the audio content of a host's media stream and transcribes it into written words in real time. This Java project demonstrates you to set up a business server that your client uses to start and stop Real-Time Transcription tasks.

## Understand the tech

To start transcribing the audio in a channel in real-time, you send an `HTTP` request to the Agora SD-RTN™ through your business server. Real-Time Transcription provides the following modes:

* Transcribe speech in real-time, then stream this data to the channel.
* Transcribe speech in real-time, store the text in the `WebVTT` format, and upload the file to third-party cloud storage.

Real-Time Transcription transcribes at most three speakers in a channel. When there are more than three speakers, the top three are selected based on volume, and their audio is transcribed.

The following figure shows the workflow to start, query, and stop a Real-Time Transcription task:

![Real-Time Transcription business server](https://docs-git-milestone37-speech-to-text-Agora-gdxe.vercel.app/en/assets/images/real-time-transcription-server-07d073102bebc8cf0f41e6985efc56b1.svg)

In order to use the RESTful API to transcribe speech, you make the following calls:

1. `acquire`: Request a `builderToken` that authenticates the user and gives permission to start Real-Time Transcription . You must call
`start` using this `builderToken` within five minutes.
1. `start`: Begin the transcription task. Once you start a task, `builderToken` remains valid for the entire
session. Use the same `builderToken` to query and stop the task.
1. `query`: Check the task status.
1. `stop`: Stop the transcription task.

## Prerequisites

In order to set up Real-Time Transcription in your app, you must have:

* Java Development Kit (JDK) 1.8 or higher.
* Installed [Maven](https://maven.apache.org/download.cgi) on your system.
* An Integrated Development Environment (IDE) to work with java project code. 
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

### Compile and run the business server

To set up the sample Real-time Transcription business server, take the following steps:

1. **Download the Git repository**

    Clone the `agora-rtt-server` repository to your development device using the following command:

    ```bash
    git clone https://github.com/saudsami/agora-rtt-server <your download directory>
    ```

1. Add the Agora dynamic key code to your project

  Copy the folder `Tools/DynamicKey/AgoraDynamicKey/java/src/main/java/io` from the downloaded repository to the root folder of your Java project.

  ```bash
  cp -r <your download directory>/Tools/DynamicKey/AgoraDynamicKey/java/src/main/java/io <your project folder>
  ```


## Test your implementation


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

