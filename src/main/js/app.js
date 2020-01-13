'use strict'

import _ from 'lodash';
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/js/all'
import 'core-js/features/set';
import { PriceFilterModal } from './modal/price-filter-modal';
import { CategoryFilterModal } from './modal/category-filter-modal';
import { Container } from './product/container';
import { Menu } from './nav/menu';
import { ProductSnapshotHistoryModal } from './modal/product-snapshot-history-modal';
/*
7. daily unique report pages with navigation
*/

var $ = require('jquery');
const React = require('react');
const ReactDOM = require('react-dom');
const Cookies = require('js-cookie');
const DOMAIN = window.location.hostname;

class Monitor extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      currentSnapshotId: "",
      latestSnapshotId: "",
      snapshotProducts: null,
      snapshotTimestamp: null,
      priceFilter: {minPrice: 0, maxPrice: Number.MAX_SAFE_INTEGER, minDiscount: 65, maxDiscount: 100},
      categoryFilter: {},
      currentJQXHR: null,
      refreshIntervalId: null,
      search: {},
      productSnapshotHistory: {}
    };
  }

  initCategoryFilter() {
    let categoryFilterCookie = Cookies.get('categoryFilter');
    let categoryFilter = null;
    if (categoryFilterCookie === null || categoryFilterCookie === undefined) {
      this.getDefaultCategories();
    } else {
      let categoryFilter = JSON.parse(categoryFilterCookie);
      this.setState({categoryFilter: categoryFilter});
      Cookies.set('categoryFilter', JSON.stringify(categoryFilter), {path: '/', domain: DOMAIN});
    }
  }

  initPriceFilter() {
	  const defaultPriceFilter = this.state.priceFilter;
	  let priceFilterCookie = Cookies.get('priceFilter');
	  if (priceFilterCookie === null || priceFilterCookie === undefined) {
		  priceFilterCookie = {};
	  } else {
      priceFilterCookie = JSON.parse(priceFilterCookie);
    }

	  const priceFilter = Object.assign({}, defaultPriceFilter, priceFilterCookie);
	  this.setState({priceFilter: priceFilter});
	  Cookies.set('priceFilter', JSON.stringify(priceFilter), {path: '/', domain: DOMAIN});
  }

  refresh() {
    let currentLatestSnapshotId = this.state.latestSnapshotId;
    let nextSnapshotId = currentLatestSnapshotId + 1;
    console.log("Refreshing with snapshot id: " + nextSnapshotId);
    $.ajax({
      dataType: "json",
      url: '/snapshot/' + nextSnapshotId,
      success: (data, textStatus, jqXHR) => {
        console.log("From success...");
        let snapshotId = data['id'];
  		  let snapshotTimestamp = data['snapshotDate'];
  		  let snapshotProducts = data['snapshotProducts'];
        let currentSnapshotId = this.state.currentSnapshotId;
        let latestSnapshotId = this.state.latestSnapshotId;

        if (currentSnapshotId < latestSnapshotId) {
          this.setState({
            latestSnapshotId: snapshotId
          });
        } else {
    		  this.setState({
            currentSnapshotId: snapshotId,
    			  snapshotTimestamp: snapshotTimestamp,
    				snapshotProducts: snapshotProducts,
            latestSnapshotId: (snapshotId > this.state.latestSnapshotId ? snapshotId : this.state.latestSnapshotId)
          });
        }
      }
    });
  }

  handleSearchBoxOnFocus() {
    console.log("handleSearchBoxOnFocus...");
    let searchObject = this.state.search;
    if (searchObject.showDropdown != true && searchObject != null
    		&& searchObject.keyword != null && searchObject.keyword.length > 0
    		&& searchObject.productSearchResult != null && searchObject.productSearchResult.length > 0) {
    	let search = Object.assign({}, this.state.search, {showDropdown: true});

        this.setState({
          search: search
        });
    }
  }

  handleSearchBoxOnBlur() {
    console.log("handleSearchBoxOnBlur...");
    setTimeout(() => {
    	if (this.state.search) {
		    let search = Object.assign({}, this.state.search, {showDropdown: false});
		    this.setState({
		      search: search
		    });
    	}
    }, 200);
  }

  handleProductSearch(keyword) {
  	console.log("handleProductSearch...");
  	keyword = keyword != null ? keyword.trim() : '';
  	if (keyword.length > 0) {
  		this.getProductSearchResult(keyword);
  		this.handleSearchBoxOnFocus();
  		return;
  	}

  	let search = Object.assign({}, this.state.search, {productSearchResult: [], keyword: null});
  	this.setState({
  		search: search
  	});
  	this.handleSearchBoxOnBlur();
  }

  getDefaultCategories() {
    $.ajax({
      dataType: 'json',
      url: '/defaultCategory',
      success: (data, textStatus, jqXHR) => {
        this.setState({
          categoryFilter: data
        });
        Cookies.set('categoryFilter', JSON.stringify(data), {path: '/', domain: DOMAIN});
      }
    });
  }

  getProductSearchResult(keyword) {
    $.ajax({
      dataType: 'json',
      url: '/productSearchResult',
      data: {'keyword' : keyword},
      success: (data, textStatus, jqXHR) => {
        console.log('From getProductSearchResult success...');
        let search = Object.assign({}, this.state.search, {productSearchResult: data, keyword: keyword});

        this.setState({
          search: search
        });
      },
      beforeSend: (jqXHR, settings) => {
        console.log('From getProductSearchResult beforeSend...');
        let currentJQXHR = this.state.search.currentJQXHR;
        if (currentJQXHR != null) {
          console.log("Abort previous product search request at URL: " + settings.url);
          currentJQXHR.abort();
        }
      },
      complete: (jqXHR, textStatus) => {
        console.log("From getProductSearchResult complete...");
        let search = Object.assign({}, this.state.search, {currentJQXHR: null});
        this.setState({search: search});
      },
      error: (jqXHR, textStatus, errorThrown) => {
    	console.log("From getProductSearchResult error...");
    	let search = Object.assign({}, this.state.search, {productSearchResult: [], keyword: keyword});
    	this.setState({search: search});
      }
    });
  }

  getProductSnapshotHistory(productId) {
	  $.ajax({
      dataType: 'json',
      url: '/productSnapshotHistory',
      data: {'productId' : productId},
      success: (data, textStatus, jqXHR) => {
        console.log('From getProductSnapshotHistory success...');
        let productSnapshotHistory = Object.assign({}, this.state.productSnapshotHistory, {history: data});
        this.setState({productSnapshotHistory: productSnapshotHistory});
      },
      beforeSend: (jqXHR, settings) => {
        console.log('From getProductSnapshotHistory beforeSend...');
        let currentJQXHR = this.state.productSnapshotHistory.currentJQXHR;
        if (currentJQXHR != null) {
          console.log('Abort previous product snapshot history request at URL: ' + settings.url);
          currentJQXHR.abort();
        }
      },
      complete: (jqXHR, textStatus) => {
        console.log('From getProductSnapshotHistory complete...');
        let productSnapshotHistory = Object.assign({}, this.state.productSnapshotHistory, {currentJQXHR: null});
        this.setState({productSnapshotHistory: productSnapshotHistory});
      },
      error: (jqXHR, textStatus, errorThrown) => {
        console.log('From getProductSnapshotHistory error...');
        let productSnapshotHistory = Object.assign({}, this.state.productSnapshotHistory, {history: null});
        this.setState({productSnapshotHistory: productSnapshotHistory});
      }
    });
  }

  handleProductSearchDropdownItemOnClick(productId) {
    this.getProductSnapshotHistory(productId);
  }

  getLatestSnapshot() {
    $.ajax({
      dataType: "json",
      url: '/latestSnapshot',
      success: (data, textStatus, jqXHR) => {
        console.log("From getLatestSnapshot.success...");
        let snapshotId = data['id'];
  		  let snapshotTimestamp = data['snapshotDate'];
  		  let snapshotProducts = data['snapshotProducts'];

        console.log("Setting up refresh every 5 seconds");
        let refreshIntervalId = setInterval(() => { this.refresh(); }, 5000);

  		  this.setState({
          currentSnapshotId: snapshotId,
  			  latestSnapshotId: snapshotId,
  			  snapshotTimestamp: snapshotTimestamp,
  				snapshotProducts: snapshotProducts,
          refreshIntervalId: refreshIntervalId
        });
      },
      beforeSend: (jqXHR, settings) => {
        console.log("From getLatestSnapshot.beforeSend...");
        let currentJQXHR = this.state.currentJQXHR;
        if (currentJQXHR != null) {
          console.log("Abort previous request at URL: " + settings.url);
          currentJQXHR.abort();
        }
        this.setState({currentJQXHR: jqXHR});
      },
      complete: (jqXHR, textStatus) => {
        console.log("From getLatestSnapshot.complete...");
        this.setState({currentJQXHR: null});
      }
    });
  }

  componentDidMount() {
	  console.log("From Monitor.componentDidMount ");
	  this.initPriceFilter();
    this.initCategoryFilter();
	  this.getLatestSnapshot();
  }

  handleSnapshotChange(snapshotId) {
    let currentSnapshotId;
    if (snapshotId >= 1 && snapshotId <= this.state.latestSnapshotId) {
      currentSnapshotId = snapshotId;
    } else {
      currentSnapshotId = this.state.latestSnapshotId;
    }

    this.setState({currentSnapshotId: currentSnapshotId});

    this.getSnapshot(currentSnapshotId);

    console.log('From function handleSnapshotChange; current snapshot ID is ' + currentSnapshotId);
  }

  getSnapshot(id) {
    $.ajax({
      dataType: "json",
      url: '/snapshot/' + id,
      success: (data, textStatus, jqXHR) => {
        console.log("From success...");
        let snapshotId = data['id'];
  		  let snapshotTimestamp = data['snapshotDate'];
  		  let snapshotProducts = data['snapshotProducts'];

  		  this.setState({
  			  snapshotTimestamp: snapshotTimestamp,
  				snapshotProducts: snapshotProducts
        });
      },
      beforeSend: (jqXHR, settings) => {
        console.log("From beforeSend...");
        let currentJQXHR = this.state.currentJQXHR;
        if (currentJQXHR != null) {
          console.log("Abort previous request at URL: " + settings.url);
          currentJQXHR.abort();
        }
        this.setState({currentJQXHR: jqXHR, currentSnapshotId: id});
      },
      complete: (jqXHR, textStatus) => {
        console.log("From complete...");
        this.setState({currentJQXHR: null});
      }
    });
  }

  handlePreviousClick() {
    let currentSnapshotId = this.state.currentSnapshotId;
    if (currentSnapshotId > 1) {
      currentSnapshotId--;
      this.getSnapshot(currentSnapshotId);
    }

    console.log('From function handlePreviousClick; current snapshot ID is ' + currentSnapshotId);
  }

  handleNextClick() {
    let currentSnapshotId = this.state.currentSnapshotId;
    if (this.state.currentSnapshotId < this.state.latestSnapshotId) {
      currentSnapshotId++;
      this.getSnapshot(currentSnapshotId);
    }

    console.log('From function handleNextClick; current snapshot ID is ' + currentSnapshotId);
  }

  handlePriceFilterChange(criteria) {
    let priceFilter = Object.assign({}, this.state.priceFilter, criteria);

    this.setState({priceFilter: priceFilter});
    Cookies.set('priceFilter', JSON.stringify(priceFilter), {path: '/', domain: DOMAIN});
  }

  handleCategoryFilterChange(brandId, genderId, categoryId, checked) {
    console.log("From handleCategoryFilterChange: " + brandId + "-" + genderId + "-" + categoryId + "-" + checked);
    let categoryFilter = Object.assign({}, this.state.categoryFilter);
    if (checked === true) {
      if (!categoryFilter.hasOwnProperty(brandId)) {
        categoryFilter[brandId] = {};
      }

      if (!(categoryFilter[brandId]).hasOwnProperty(genderId)) {
        categoryFilter[brandId][genderId] = [];
      }

      if ($.inArray(categoryId, categoryFilter[brandId][genderId]) === -1) {
        categoryFilter[brandId][genderId].push(categoryId);
      }
    } else {
      if (categoryFilter.hasOwnProperty(brandId)) {
        if (categoryFilter[brandId].hasOwnProperty(genderId)) {
          if ($.isArray(categoryFilter[brandId][genderId])) {
            categoryFilter[brandId][genderId] = $.grep(categoryFilter[brandId][genderId], function(e, i) {
              return e != categoryId;
            });
          }
        }
      }
    }

    this.setState({categoryFilter: categoryFilter});
    Cookies.set('categoryFilter', JSON.stringify(categoryFilter), {path: '/', domain: DOMAIN});
  }

  render() {
    return (
      <div className="monitor">
        <Menu
          currentSnapshotId={this.state.currentSnapshotId}
          latestSnapshotId={this.state.latestSnapshotId}
          onPreviousClick={() => this.handlePreviousClick()}
          onNextClick={() => this.handleNextClick()}
          onSnapshotChange={(snapshotId) => this.handleSnapshotChange(snapshotId)}
          snapshotTimestamp={this.state.snapshotTimestamp}
          isLoading={this.state.currentJQXHR != null}
          showProductSearchDropdown={this.state.search.showDropdown}
          onProductSearchBoxFocus={() => this.handleSearchBoxOnFocus()}
          onProductSearchBoxBlur={() => this.handleSearchBoxOnBlur()}
          onProductSearchBoxChange={(keyword) => this.handleProductSearch(keyword)}
          productSearch={this.state.search}
          onProductSearchDropdownItemClick={(productId) => this.handleProductSearchDropdownItemOnClick(productId)}
        />
        <PriceFilterModal priceFilter={this.state.priceFilter}
          onPriceFilterChange={(criteria) => this.handlePriceFilterChange(criteria)} />
        {this.state.snapshotProducts != null &&
          <CategoryFilterModal categoryFilter={this.state.categoryFilter}
            snapshotProducts={this.state.snapshotProducts}
            onCategoryFilterChange={(brandId, genderId, categoryId, checked) => this.handleCategoryFilterChange(brandId, genderId, categoryId, checked)} />}
        <ProductSnapshotHistoryModal productSnapshotHistory={this.state.productSnapshotHistory.history}
          onSnapshotChange={(snapshotId) => this.handleSnapshotChange(snapshotId)} />
        <Container snapshotProducts={this.state.snapshotProducts}
          priceFilter={this.state.priceFilter}
          categoryFilter={this.state.categoryFilter}
          onProductSearchDropdownItemClick={(productId) => this.handleProductSearchDropdownItemOnClick(productId)} />
      </div>
    );
  }
}

ReactDOM.render(<Monitor />, $('#root').get(0));

$(function(){

  $( "#productSnapshotHistoryModal" ).on('hidden.bs.modal', function(){

  });

});
