import fetch from "node-fetch";
const USER_ID = process.env.REACT_APP_USERID;
//const USER_ID='358CLEAR3801';
const BASE_URI =  "https://production.shippingapis.com/ShippingAPITest.dll?API=CityStateLookup&XML=";
const config = {
  headers: {
    "Content-type": "text/html",
    "Access-Control-Allow-Origin":"*",
    "Access-Control-Allow-Credentials": true,
    "Access-Control-Allow-Methods": "GET",
  },
  method: "get",
};
export async function handler (event, context) {
  // zipcode from frontend
  const zipcode = event.queryStringParameters.zipcode;
  // xml sent to USPS request
  const xml = `<CityStateLookupRequest USERID="${USER_ID}"><ZipCode>
  <Zip5>${zipcode}</Zip5></ZipCode></CityStateLookupRequest>`;
  try {
    console.log(BASE_URI);
    console.log(xml);
    const response = await fetch(`${BASE_URI}${xml}`,config);
    if (!response.ok ) {
      return { statusCOde: response.staus, body: response };
    }
    const data = await response.text();
    return {
      statusCode: 200,
      body: data,
    };
  } catch (err) {
    console.log("Error", err);
    return {
      statusCode: 500,
      body: JSON.stringify({ msg: err.message }),
    };
  }

}
