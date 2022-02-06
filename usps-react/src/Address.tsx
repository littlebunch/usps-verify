
import React, { useState, useEffect } from "react";
import { ObjectType, ObjectTypeDeclaration } from "typescript";
import { useForm } from 'react-hook-form';
import "./App.css";
import {FieldLabel,FieldError,Fieldset,FieldInput,ActionButton,SubmissionSuccess,SubmissionFailure} from './Styles';

type FormData = {
    address1: string,
    address2: string,
    zipcode: string,
    city: string,
    state: string,
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
  const [address1,setAddress1] = useState("");
  const [address2,setAddress2] = useState("");
  const initialCityState = {city: "", state: "" };
  const [cityState, setCityState] = useState(initialCityState);
  const [zipcode, setZipcode] = useState("");
  const [loading, setLoading] = useState(false);
  const isZipValid = zipcode.length === 5 && zipcode;
  const parser = new DOMParser();
  const [successfullySubmitted, setSuccessfullySubmitted] =
  React.useState(false);
  const [unsuccessfullySubmitted, setUnsuccessfullySubmitted] =
  React.useState(false);
  const {handleSubmit,formState:{errors}, setError, clearErrors, formState, register,reset } = useForm<FormData>({
    mode: 'onBlur',
});
  useEffect(() => {
    if ( isZipValid ) {
        fetchCityState();
    }
  },[zipcode]);
async function fetchCityState ()  {
    try {
        const response = await fetch(
          `/.netlify/functions/getCityState?zipcode=${zipcode}`,
          {headers: { accept: "application/json"}}
        );
        const data = await response.text();
        setLoading(false);
        const srcDom=parser.parseFromString(data,"application/xml");
        const res=xml2json(srcDom);
        if ( res?.CityStateLookupResponse?.ZipCode?.City) {
          setLoading(false);
          clearErrors("zipcode");
          setCityState({...cityState, 
            city: res.CityStateLookupResponse.ZipCode.City, 
            state: res.CityStateLookupResponse.ZipCode.State,
          });
        } else if (res?.CityStateLookupResponse?.ZipCode?.Error) {
          setLoading(false);
          setError("zipcode",{message:"Please enter a valid zipcode."})
          setCityState({
            ...cityState,
            city: "",
            state: "",
          });
        
      }
    } catch(e) {
      console.log(e);
    }
  };
  
 async function validateAddress() {
    const response = await fetch(
        `/.netlify/functions/validateForm?zipcode=${zipcode}&address1=${address1}&address2=${address2}`,
        {headers: { accept: "application/json"}}
    );
    const data = await response.text();
    setLoading(false);
    const srcDom=parser.parseFromString(data,"application/xml");
    const res=xml2json(srcDom);
    if ( res.AddressValidateResponse.Address.Error) {
        setUnsuccessfullySubmitted(true);
         setSuccessfullySubmitted(false);
    } else {
        if ( !res.AddressValidateResponse.Address.Address1  ) {
            setAddress1(res.AddressValidateResponse.Address.Address2);
            setAddress2("");
        } else {
            console.log(res.AddressValidateResponse.Address.Address2);
            setAddress1(res.AddressValidateResponse.Address.Address1);
            setAddress2(res.AddressValidateResponse.Address.Address2);
        }
        setSuccessfullySubmitted(true);
        setUnsuccessfullySubmitted(false);
    }
 }
 
  return (
  <div>
    <h1>USPS Address API</h1>
    <Fieldset disabled={formState.isSubmitting }>
    <form onSubmit={handleSubmit(validateAddress)} >
   <FieldLabel htmlFor="address1">Address 1</FieldLabel>
      <FieldInput
      value = {address1}
      type="text"
      name="address1"
      id="address1"
      width={"25%"}
      onChange={(event) => {
        const { value } = event.target;
        setAddress1(value);
      }}/>
      <br/>
      <FieldLabel htmlFor="address2">Address 2</FieldLabel>
      <FieldInput
      value={address2}
      type="text"
      name="address2"
      id="address2"
      width={"25%"}
      onChange={(event) => {
        const { value } = event.target;
        setAddress2(value);
      }}/>
      <br/>
      <FieldLabel htmlFor="zipcode">Zip Code</FieldLabel>
      <FieldInput
      value={zipcode || ""}
      placeholder="XXXXX"
      type="text"
      id="zipcode"
      width={"5%"}
      {...register("zipcode",{required: "Please enter a valid zipcode.",
      })}
      onChange={(event) => {
        const { value } = event.target;
        setLoading(true);
        setZipcode(value.replace(/[^\d{5}]$/,"").substring(0,5));
      }}
      />
      
      <FieldLabel htmlFor="city">City</FieldLabel>
      <FieldInput
      value={cityState.city}
      type="text"
      name="city"
      disabled
      id="city"
      width={"20%"}
      />
      <div className="icon-container">
            <i className={`${loading && isZipValid ? "loader" : ""}`}></i>
          </div>
      <FieldLabel htmlFor="state">State</FieldLabel>
      <FieldInput
      value={cityState.state}
      type="text"
      name="state"
      disabled
      id="state"
      width={"5%"}
      />
      {errors.zipcode && <FieldError>{errors.zipcode.message}</FieldError>}
      <br/>
      
    
      <ActionButton type="submit" onClick={()=> clearErrors()}
      color="#4CAF50">Validate</ActionButton>
      
          <ActionButton type='reset'
          onClick={() => reset()}
            color="red" >Reset</ActionButton>
        {successfullySubmitted && (
            <SubmissionSuccess>
              ADDRESS VALIDATED
            </SubmissionSuccess>
          )}
          {unsuccessfullySubmitted && (
              <SubmissionFailure>
                  ADDRESS NOT VALIDATED
              </SubmissionFailure>
          )}
   </form>
   </Fieldset>

    
  </div>
  );
}