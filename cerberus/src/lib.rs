extern crate pretty_env_logger;

mod ws;

pub async fn start() {
    pretty_env_logger::init();

    ws::init().await;
}