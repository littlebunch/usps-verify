extern crate diesel;
extern crate serde;
#[macro_use]
use dotenv::dotenv;
use serde::{Deserialize, Serialize};
use std::env;

use diesel::mysql::MysqlConnection;
use ingest_rs::csv::{process_derivations, process_foods, process_nutrients,process_nutdata};
use ingest_rs::models::Food;
use std::process;

//#[derive(Debug, Serialize, Deserialize)]
/// Config is the data source configuration.
pub struct Config {
    pub url: String,
}

impl Config {
    pub fn new() -> Self {
        dotenv().ok();
        let database_url = env::var("DATABASE_URL").expect("Bad url");
        Self { url: database_url }
    }
    pub fn establish_connection(&self) -> MysqlConnection {
        MysqlConnection::establish(&self.url).expect(&format!("Error connecting to {}", self.url))
    }

    ///
    ///  rudimentary implementation of query
    pub fn run(&self, csvtype: &str, path: &str) {
        let conn = self.establish_connection();
        match csvtype {
            "BFPD" => {
                let mut count:usize = 0;
                println!("Loading foods");
                count = process_foods(path.to_string(), &conn);
                println!("Finished. {} foods loaded.", count);
                println!("Now loading nutrient data.");
                count = process_nutdata(path.to_string(),&conn);
                println!("Finished. {} nutrient data.",count)
            }
            "NUT" => {
                let count = process_nutrients(path.to_string(), &conn);
                println!("Finished.  {} nutrients loaded", count);
            }
            "DERV" => {
                let count = process_derivations(path.to_string(), &conn);
                println!("Finished.  {} derivations loaded", count);
            }
            _ => {
                println!("invalid input type");
                process::exit(1)
            }
        }

        process::exit(0)
    }
}
