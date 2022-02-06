import fetch from "node-fetch";
const USER_ID = process.env.REACT_APP_USERID;
const BASE_URI =  "https://production.shippingapis.com/ShippingAPITest.dll?API=Verify&XML=";
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
  const address1 = event.queryStringParameters.address1;
  const address2 = event.queryStringParameters.address2;
  // xml sent to USPS request
  const xml = `<AddressValidateRequest USERID="${USER_ID}"><Address ID="0" >
<Address1>${address1}</Address1><Address2>${address2}</Address2><City/><State/><Zip5>${zipcode}</Zip5><Zip4/></Address></AddressValidateRequest>`;
try {
    console.log(BASE_URI);
    console.log(xml);
    const response = await fetch(`${BASE_URI}${xml}`,config);
    if (!response.ok ) {
      return { statusCOde: response.staus, body: response };
    }
    const data = await response.text();
    console.log(data);
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
