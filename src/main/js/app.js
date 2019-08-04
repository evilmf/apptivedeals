'use strict'

import _ from 'lodash';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/js/all'


var $ = require('jquery');
const React = require('react');
const ReactDOM = require('react-dom');

class HelloMessage extends React.Component {
  render() {
    return <p className="text-warning">Hello webpack</p>;
  }
}

//ReactDOM.render(<HelloMessage />, $('body').get(0));