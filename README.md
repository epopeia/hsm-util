# HSM Utility

## Inform the HSM command buffer for directly execution instead of CLI options:

`java -jar ./target/hsm-util-0.0.1-SNAPSHOT.jar "command buffer"`

## Customize the jar execution with this options if necessary (this example values are already default)

-Dhsm.host=localhost

-Dhsm.port=3001

-Dhsm.header.length=2

-Dhsm.lmk=%01
