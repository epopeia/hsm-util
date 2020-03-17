# HSM Utility

## Inform the HSM command buffer for directly execution instead of CLI options:

`java -jar ./target/hsm-util-0.0.1-SNAPSHOT.jar "command buffer"`

## Customize the jar execution with this options if necessary (this example values are already default)

-Dhsm.host=localhost

-Dhsm.port=3001

-Dhsm.header.length=2

## To run with Docker use:

`docker run registry.gitlab.com/bit55/public/hsm-util/hsm-util "command buffer goes here"`

or run in an iterative mode with menu options:

`docker run -it registry.gitlab.com/bit55/public/hsm-util/hsm-util`

