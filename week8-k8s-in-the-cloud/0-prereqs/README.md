![banner](https://raw.githubusercontent.com/DataStax-Academy/cassandra-workshop-series/master/materials/images/banner2.png)


# Prerequisites
To follow these steps, you will need to have your own GCP account. 

A GCP account can be created for free, and with a new account you get a one-year free trial phase with $300 worth of credits to experiment with the GCP products. You will need some of these credits to follow along. 

The cost of having a small kubernetes cluster for a few days came to about $3-4 per day.

⚠️ Important:
You will need your own cloud account. This is not an exercise that you can do locally or with a loaned cloud instance.

## Setup steps
### 1 - Create GCP account

Sign in or set up a new account on Google Cloud Platform:
https://cloud.google.com/

You will need to provide a valid credit card in order to complete the account setup. If you cannot provide a credit card or bank account, you will not be able to proceed with this exercise. In this case, just watch and learn for future reference. 

Once you are signed in, proceed to the Console. The Console is your starting point in GCP, and we will get back to the Console again and again.

### 2 - Create a project

A first default project is created for you, named `My First Project`. We are using this project to host our Kubernetes cluster. 

### 3 - Billing
You entered your credit card, so you want to keep a good eye on the costs, even during the free trial. One way is to export your billing. 

You can access the billing overview from the Console in the left hand side panel. 

Reports shows how much you've been charged so far.

<img width="522" alt="Screenshot 2020-08-16 at 04 53 08" src="https://user-images.githubusercontent.com/20337262/90419685-d757ac80-e0ae-11ea-8f25-25410f05f405.png">

To get a more detailed understanding of the cost, we set up daily billing exports to csv. Before we can enable billing export, we need to create a storage bucket where the csv files will be exported to. 

#### 3a - Set up billing storage bucket

On the left hand side panel in the Console (accessible by the hamburger button in the top left corner), select Storage:

<img width="526" alt="Screenshot 2020-08-16 at 04 57 14" src="https://user-images.githubusercontent.com/20337262/90419818-040bc400-e0af-11ea-9948-7ab61c690a7c.png">

Go into the storage browser and create a bucket:

⚠️  The bucket name is globally unique.

<img width="583" alt="Screenshot 2020-08-16 at 04 58 05" src="https://user-images.githubusercontent.com/20337262/90419884-1980ee00-e0af-11ea-8524-8f7cd2ab70fc.png">

Choose a unique name (we chose workshop-billing) and accept all defaults. This is how the configuration looks in my case:

<img width="600" alt="Screenshot 2020-08-16 at 04 59 04" src="https://user-images.githubusercontent.com/20337262/90419956-374e5300-e0af-11ea-899a-f9adc4bc2628.png">

Remember its name, we will give it to the billing export.

#### 3b - Set up billing export
On the left hand side panel in the Console, go back to Billing, select Billing export and then FILE EXPORT

Configure to use your created bucket for daily exports. You will have to wait a day before you can see an export.

<img width="711" alt="Screenshot 2020-08-16 at 05 05 20" src="https://user-images.githubusercontent.com/20337262/90419999-47fec900-e0af-11ea-9242-99aa1fa5b624.png">

### 4 - Create your Google Kubernetes Engine cluster
On the left hand side panel in the Console, go to Kubernetes Engine > Clusters

<img width="451" alt="Screenshot 2020-08-16 at 05 09 18" src="https://user-images.githubusercontent.com/20337262/90420046-551bb800-e0af-11ea-8817-4862c6322305.png">

Select `CREATE CLUSTER`

For this exercise, we following the guide for the recommended starter cluster:

<img width="600" alt="Screenshot 2020-08-16 at 05 15 11" src="https://user-images.githubusercontent.com/20337262/90420081-649b0100-e0af-11ea-8372-afec9e250e2e.png">

Select `CREATE NOW`

It takes a bit of time until this is created (3-5mins). Here you see `my-first-cluster-1` being created.

<img width="600" alt="Screenshot 2020-08-18 at 11 09 04" src="https://user-images.githubusercontent.com/20337262/90500909-7332f800-e143-11ea-9392-fe597b65b835.png">

Once created, you will see the allocated resource:

<img width="600" alt="Screenshot 2020-08-18 at 11 13 04" src="https://user-images.githubusercontent.com/20337262/90501159-d0c74480-e143-11ea-930f-3f27b948a11f.png">

Click on the `Connect` button to open a console terminal to control this cluster

Select to `Run in Cloud Shell`:

<img width="600" alt="Screenshot 2020-08-16 at 05 22 27" src="https://user-images.githubusercontent.com/20337262/90420274-a5931580-e0af-11ea-934d-0f23f9623d47.png">

<img width="600" alt="Screenshot 2020-08-16 at 05 23 27" src="https://user-images.githubusercontent.com/20337262/90420298-adeb5080-e0af-11ea-892c-cc6f7bd2b84a.png">

## Congratulations, you created your cluster. Now, let's explore nodes and pods [HERE](../1-pods-and-nodeports).
