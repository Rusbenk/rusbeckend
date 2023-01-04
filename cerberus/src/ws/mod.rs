use std::net::SocketAddr;

use warp::Filter;

mod hello;

pub async fn init() {
    let routes = warp::any().and(
        hello::hello()
            .or(hello::hello2()));

    let addr: SocketAddr = "0.0.0.0:8000".parse().unwrap();
    warp::serve(routes).run(addr).await
}