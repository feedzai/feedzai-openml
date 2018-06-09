# How to contribute to OpenML

This repository is set up to work under the traditional fork + Pull Request model.

When tags are pushed on the repo the deploy target will be built automatically by Travis and the jars
signed and pushed to Sonatype.

## Deploying from a local machine

First of all it is important to realise that [jgitver](https://github.com/jgitver/jgitver) will calculate the version 
to deploy based on Git history. 
If you want to deploy a `MAJOR.MINOR.PATCH` version you must be in the `master` branch
with `HEAD` on an annotated tag (`git tag -a -m "<your message>" XXX.YYY.ZZZ`) or use `-Djgitver.use-version=XXX.YYY.ZZZ`. 

To sign and deploy the artifacts locally you need to define the following environment variables (they are usually read 
from Travis):
* `SONATYPE_USER`: Feedzai's Sonatype username (usually `feedzai`)
* `SONATYPE_PASS`: Feedzai's Sonatype password
* `PGP_KEY_ID`: The ID of the PGP key to sign the artifacts with
* `PGP_PASS`: The password of the private PGP key

To access Sonatype with the correct credentials you should run the command:

`mvn clean deploy -Prelease --settings .m2/settings.xml` 

**Note:**
When your local system has GPG 2.1 or later installed the gpg executable will try to migrate the secret keys
to the new (non-keyring) format before using them which might fail if being done during maven targets.
As such, it is better to explicitly force that migration *before* by using a command like:

`gpg -K --homedir=.gnupg`

