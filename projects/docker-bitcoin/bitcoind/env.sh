export HOST_DATA_DIR_PREFIX="/var/lib/bitcoind-docker"
export HOST_BITCOIND_DATA_DIR="$HOST_DATA_DIR_PREFIX/bitcoind"
export HOST_BITCOIND_P2P_PORT=8333
export HOST_BITCOIND_RPC_PORT=8332

export BITCOIND_RPC_USER=
export BITCOIND_RPC_PASSWORD=
export BITCOIND_CONFIG=

export BITCOIND_NODE_CONTAINER_NAME=bitcoind
export BITCOIND_RPC_CONTAINER_NAME=bitcoind-rpc

# Overrides.
#if [ -e .env ]; then
#    source .env
#fi
