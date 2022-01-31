mod ds;
#[macro_use]
extern crate clap;
use clap::App;
use ds::Config;
use std::process;

fn main() {
    let cli = load_yaml!("clap.yml");
    let config = Config::new();
    let matches = App::from_yaml(cli).get_matches();
    let csvtype = matches.value_of("type").unwrap();
    let path = matches.value_of("path").unwrap();
    config.run(&csvtype, &path);
}
