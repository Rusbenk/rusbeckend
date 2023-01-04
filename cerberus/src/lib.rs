mod ws;

pub async fn start() {
    ws::init().await;
}