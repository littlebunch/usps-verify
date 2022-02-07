import styled from '@emotion/styled';
//import { css } from '@emotion/react';
type InputProp = {
  width: string
}
type ButtonProp = {
  color:string
}
export const Fieldset = styled.fieldset`
  margin: 10px auto 0 auto;
  padding: 30px;
  width: 90%;
  background-color: #f7f8fa;
  border-radius: 4px;
  border: 1px solid #e3e2e2;
  color: #212529;
  box-shadow: 0 3px 5px 0 rgba(0, 0, 0, 0.16);
`;
export const ActionButton = styled.button<ButtonProp>`
  background-color: ${props =>
      props.color }; 
  border: none;
  color: white;
  padding: 15px 32px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 16px;
  margin:5px;
  :hover {
    box-shadow: 0 12px 16px 0 rgba(0,0,0,0.24), 0 17px 50px 0 rgba(0,0,0,0.19);
    font-weight: bold;
  }
  focus {
    outline-color: #824c67;
  }
  :disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
  `;
  export const FieldInput = styled.input<InputProp>`
    font-size: 1.2rem;
    margin: 10px;
    text-align: center;
    justify-content: center;
    align-items: center;
    width: ${props =>
      props.width };
  `;
  export const FieldLabel = styled.label`
  font-weight: 500;
  font-size: 1.2rem;`;

  export const SubmissionSuccess = styled.div`
  margin-top: 10px;
  color: #3875a4;
  font-weight:bold;
`;
export const SubmissionFailure = styled.div`
  margin-top: 10px;
  color: red;
  font-weight: bold;
`;
export const FieldError = styled.div`
  font-size: 12px;
  color: red;
`;