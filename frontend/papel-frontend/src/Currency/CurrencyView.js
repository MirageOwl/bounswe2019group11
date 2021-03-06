import React from 'react';
import { Card } from 'react-bootstrap';

function CurrencyView({ code, name, rate, comments, predictions, userPredictions }) {

  return (
    <Card.Title style={{ textAlign: "center" }}>
      <hr />
      <Card.Title><b>{code + " (" + name + ")"} </b></Card.Title>
      <Card.Text>USD/{code} :{rate}</Card.Text>
    </Card.Title>

  );
}

export default CurrencyView;

