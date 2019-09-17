const React = require('react');

class ProductSearchDropdownItem extends React.Component {
  render() {
    return (
      <li className="list-group-item p-0">
        <a href="#" className="text-decoration-none">
          <div className="card border-0">
            <div className="row no-gutters">
              <div className="col-8">
                <div className="card-body p-1 pb-0">
                  <ul className="list-group">
                    <li className="list-group-item p-0 border-0 card-title"><small>Cascade Lake Jacket</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small>Abercrombie</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small>Top</small></li>
                    <li className="list-group-item p-0 border-0 card-text"><small className="text-muted float-left">Mens</small><small className="text-muted float-right">$10 - $20</small></li>
                  </ul>
                </div>
              </div>
              <div className="col-4">
                <img src="https://anf.scene7.com/is/image/anf/KIC_123-9103-2551-100_prod1?$product-anf-v1$&wid=200&hei=250" className="card-img rounded-0 float-right" style={{height: '6rem', width: '4rem'}} alt="Cascade Lake Jacket" />
              </div>
            </div>
          </div>
        </a>
      </li>
    );
  }
}

class ProductSearchDropdown extends React.Component {
  render() {
    return (
      <ul className="list-group position-absolute px-3 w-100 d-none">
        <li className="list-group-item p-0">
          <a href="#" className="text-decoration-none">
            <div className="card border-0">
              <div className="row no-gutters">
                <div className="col-8">
                  <div className="card-body p-1 pb-0">
                    <ul className="list-group">
                      <li className="list-group-item p-0 border-0 card-title"><small>Cascade Lake Jacket</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Abercrombie</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Top</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small className="text-muted float-left">Mens</small><small className="text-muted float-right">$10 - $20</small></li>
                    </ul>
                  </div>
                </div>
                <div className="col-4">
                  <img src="https://anf.scene7.com/is/image/anf/KIC_123-9103-2551-100_prod1?$product-anf-v1$&wid=200&hei=250" className="card-img rounded-0 float-right" style={{height: '6rem', width: '4rem'}} alt="Cascade Lake Jacket" />
                </div>
              </div>
            </div>
          </a>
        </li>
        <li className="list-group-item p-0">
          <a href="#" className="text-decoration-none">
            <div className="card border-0">
              <div className="row no-gutters">
                <div className="col-8">
                  <div className="card-body p-1 pb-0">
                    <ul className="list-group">
                      <li className="list-group-item p-0 border-0 card-title"><small>Cascade Lake Jacket</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Abercrombie</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Top</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small className="text-muted float-left">Mens</small><small className="text-muted float-right">$10 - $20</small></li>
                    </ul>
                  </div>
                </div>
                <div className="col-4">
                  <img src="https://anf.scene7.com/is/image/anf/KIC_123-9103-2551-100_prod1?$product-anf-v1$&wid=200&hei=250" className="card-img rounded-0 float-right" style={{height: '6rem', width: '4rem'}} alt="Cascade Lake Jacket" />
                </div>
              </div>
            </div>
          </a>
        </li>
        <li className="list-group-item p-0">
          <a href="#" className="text-decoration-none">
            <div className="card border-0">
              <div className="row no-gutters">
                <div className="col-8">
                  <div className="card-body p-1 pb-0">
                    <ul className="list-group">
                      <li className="list-group-item p-0 border-0 card-title"><small>Cascade Lake Jacket</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Abercrombie</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small>Top</small></li>
                      <li className="list-group-item p-0 border-0 card-text"><small className="text-muted float-left">Mens</small><small className="text-muted float-right">$10 - $20</small></li>
                    </ul>
                  </div>
                </div>
                <div className="col-4">
                  <img src="https://anf.scene7.com/is/image/anf/KIC_123-9103-2551-100_prod1?$product-anf-v1$&wid=200&hei=250" className="card-img rounded-0 float-right" style={{height: '6rem', width: '4rem'}} alt="Cascade Lake Jacket" />
                </div>
              </div>
            </div>
          </a>
        </li>
      </ul>
    );
  }
}

export { ProductSearchDropdown };
