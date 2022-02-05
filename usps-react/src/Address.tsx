
import React, { useState, useEffect } from "react";
import { ObjectType, ObjectTypeDeclaration } from "typescript";
import { useForm } from 'react-hook-form';
import "./App.css";
import {Fieldset} from './Styles';

type FormData = {
    city: string,
    state: string,
    zipcode: string,
    address1: string,
    address2: string 
  };

const xml2json = (srcDOM: any) => {
  var jsonResult: any = {};
  
  let children = [...srcDOM.children];
  // base case for recursion.
  if (!children.length) {
    return srcDOM.innerHTML;
  }
  // initializing object to be returned.
  
  for (let child of children) {
    // checking is child has siblings of same name.
    let childIsArray =
      children.filter((eachChild) => eachChild.nodeName === child.nodeName)
        .length > 1;
    // if child is array, save the values as array, 
    // else as strings.
    if (childIsArray) {
      if (jsonResult[child.nodeName] === undefined) {
        jsonResult[child.nodeName] = [xml2json(child)];
      } else {
        jsonResult[child.nodeName].push(xml2json(child));
      }
    } else {
      jsonResult[child.nodeName] = xml2json(child);
    }
  }
  return jsonResult;
};
export const Address = () => {
  
  const initialCityState = {city: "", state: "" };
  const [cityState, setCityState] = useState(initialCityState);
  const [zipcode, setZipcode] = useState("");
  const [loading, setLoading] = useState(false);
  const isZipValid = zipcode.length === 5 && zipcode;
  const parser = new DOMParser();
  const address1 = useState(false);
  const [successfullySubmitted, setSuccessfullySubmitted] =
  React.useState(false);
  useEffect(() => {
    const fetchCityState = async () => {
      try {
        if ( isZipValid ) {
          const response = await fetch(
            `/.netlify/functions/getCityState?zipcode=${zipcode}`,
            {headers: { accept: "application/json"}}
          );
          const data = await response.text();
          setLoading(false);
          const srcDom=parser.parseFromString(data,"application/xml");
          const res=xml2json(srcDom);
          //console.log(xml2json(srcDom));
          //console.log(srcDom);
          if ( res?.CityStateLookupResponse?.ZipCode?.City) {
            setLoading(false);
            setCityState({...cityState, 
              city: res.CityStateLookupResponse.ZipCode.City, 
              state: res.CityStateLookupResponse.ZipCode.State,
            });
          } else if (res?.CityStateLookupResponse?.ZipCode?.Error) {
            setLoading(false);
            setCityState({
              ...cityState,
              city: `Invalid Zip Code for ${zipcode}`,
              state: "Try Again",
            });
          }
        }
      } catch(e) {
        console.log(e);
      }
    };
    fetchCityState();
  },[zipcode]);
  const {handleSubmit, formState } = useForm<FormData>({
    mode: 'onBlur',
});
const submitForm = (data: FormData) => {
    // Preventing the page from reloading
    console.log(address1);
    console.log(zipcode);
    console.log(cityState.city);
    console.log(cityState.state);
    setSuccessfullySubmitted(true);
    
  }
  return (
  <div className="App">
    <h1>USPS Zip API</h1>
    <Fieldset disabled={formState.isSubmitting || successfullySubmitted}>
    <form onSubmit={handleSubmit(submitForm)} className="form-data">
      <label htmlFor="address1">Street Address 1</label>
      <input className="address"
      type="text"
      name="address1"
      id="address1"/>
      <br/>
      <label htmlFor="address2">Street Address 2</label>
      <input className="address"
      type="text"
      name="address2"
      id="address2"/>
      <br/>
      <label htmlFor="zip">Zip Code</label>
      <input
      className="zip"
      value={zipcode || ""}
      placeholder="XXXXX"
      type="text"
      name="zip"
      id="zip"
      onChange={(event) => {
        const { value } = event.target;
        setLoading(true);
        setZipcode(value.replace(/[^\d{5}]$/,"").substring(0,5));
      }}
      />
      <label htmlFor="city">City</label>
      <input
      className={`city`}
      value={cityState.city}
      type="text"
      name="city"
      disabled
      id="city"
      />
      <div className="icon-container">
            <i className={`${loading && isZipValid ? "loader" : ""}`}></i>
          </div>
      <label htmlFor="state">State</label>
      <input
      className={`state`}
      value={cityState.state}
      type="text"
      name="state"
      disabled
      id="state"
      />
      <br/>
      <button type="submit">Save</button>
      <div className="icon-container">
        <i className={`loading && isZipValue ? "loader" : ""`}>
        </i>
      </div>
    </form>

    </Fieldset>
    
  </div>
  );
}