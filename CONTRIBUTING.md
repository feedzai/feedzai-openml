# How to contribute to OpenML

This repository is set up to work under the traditional fork + Pull Request model.

## Architecture guidelines

Development of these artifacts should be done with care and taking into account that these APIs are implemented by 
multiple providers. As much as possible changes should be done in a gradual way, with older endpoints being deprecated
in one Major.Minor release and eliminated in the next.

Also, note that the model implementations are called in execution contexts where throughput could be high and latency 
is critical. Take this into account when choosing data structures and the levels of abstraction used. 

## CI/CD
Quality is validated in pull requests using [travis-ci.com](https://travis-ci.com/feedzai/feedzai-openml) using the 
configuration that you can see in the [Travis YAML](https://github.com/feedzai/feedzai-openml/blob/master/.travis.yml). 
The `script` phase of the Travis lifecycle will run tests which produce JaCoCo coverage reports that are in turn sent
to [Codecov.io](https://codecov.io) for code coverage analysis.
In parallel, Codacy runs static code analysis and displays it on [Codacy](https://app.codacy.com/app/feedzai/feedzai-openml/dashboard). 

## Merging
Pull requests with failing builds will not be merged, and coverage is expected to be above 85%.
Static code analysis issues will be evaluated ad-hoc, especially since it is common to have several warnings related to
abstraction violations that need to be performed for performance reasons.

## Releasing
**When releasing a new Major.Minor** (read about [Semantic Versioning](https://semver.org/)) maintainers need perform a few
steps to ensure the repository documents are updated:
   * Update the [README](https://github.com/feedzai/feedzai-openml/blob/master/README.md) so that the XML excerpts indicate
   the new version
   * Create a new hotfix branch named *hf-MM.mm.X* (where MM is the Major and mm the Minor)
   * In that new branch, perform a pull request to change the badge URLs to point to the new branch

**For all releases**, as the hotfix branch is ready all that's needed to actually release is to create an annotated tag 
pointing to the hotfix branch head (example below for releasing version `1.1.0`):
```bash
# Ensure the tag is made on the udpated branch
git fetch -a
git checkout origin/hf-1.1.X
git tag -a 1.1.0
# Your EDITOR will open. Write a good message and save as it is used on Github as a release message
git push origin 1.1.0
``` 

The maven versions of these modules are controlled not by what's written in the POM but by the git history - see 
[jgitver's page](https://github.com/jgitver/jgitver) for more info. As such, when annotated tags are pushed on the repo 
the deploy target will be built automatically by Travis and the jars signed and pushed to Sonatype (see [the section on what this entails](#notes-on-sonatype-deployments)).

## Deploying from a local machine
Since jgitver relies on git history, when deploying version MM.mm.PP to Maven Central from a local computer you must 
either create a local annotated tag `MM.mm.PP` pointing to your current commit or use `-Djgitver.use-version=MM.mm.PP` 
when running the maven command below. 

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

## Notes on Sonatype deployments
Deployments to sonatype may be tricky so it is important to know how they work. 

When the deployment process starts, a staging repository is created with the name `comfeedzai-XXXX`, which you can access at https://oss.sonatype.org/#stagingRepositories if you have credentials for it (order by descending creation date). Maven will be uploading all the artifacts to that staging repository as the deploy target is being executed on all nodes and at the end of the last artifact's upload a series of validation rules are performed remotely to ensure that at least all the following apply:

  * All the POMs have 
      * Name
      * URL
      * SCM information
      * Developer information
  * There are sources and javadocs jars
  * All jars are PGP-signed
  
Once all validation rules finish successfully the repository is ready for closing. Our project POMs have the `autoReleaseAfterClose` property enabled so as the process finishes and the staging repository is closed, all artifacts are also published and will eventually be included in Maven Central.

### Known issues
Sometimes the Sonatype infrastructure will not be responsive enough and timeouts will occur while performing any of the publishing steps (creating the repo, running rules, closing the repo, etc).
If this happens you should firstly look at the build logs in travis to understand the step where it failed. You can (and should) access the staging repository to understand the state of the repository and drop it.
In most cases recovery is just a matter of running the build again after a while (for Sonatype's systems to be in a healthier state). To do this you can either re-run the build via the Travis UI or delete the tag and create it again:

```bash
git push origin --delete 0.1.1
git push origin 0.1.1
```

## Notes on IDE Integration
Intellij IDEA is, at the time of this writing, incompatible with jgitver and as such it must be disabled as described 
[here](https://github.com/jgitver/jgitver-maven-plugin/wiki/Intellij-IDEA-configuration).