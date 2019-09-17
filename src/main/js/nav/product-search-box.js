const React = require('react');

class ProductSearchBox extends React.Component {
  render() {
    return (
      <div className="input-group px-3">
				<input type="text" className="form-control"
					placeholder="Search Keyword" aria-label="Product-Search" />
				<div className="input-group-append">
					<span className="input-group-text"><i className="fas fa-search"></i></span>
				</div>
        <div className="dropdown ml-3">
					<button className="btn dropdown-toggle border" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					</button>
					<div className="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
						<a className="dropdown-item" data-toggle="modal" href="#" data-target="#categoryFilterModal">Category Filter</a>
						<a className="dropdown-item" data-toggle="modal" href="#" data-target="#priceFilterModal">Price Filter</a>
					</div>
				</div>
			</div>
    );
  }
}

export { ProductSearchBox };
