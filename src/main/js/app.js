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
/*
6. search function (search drop down and popup modal)
7. daily unique report pages with navigation
8. auto refresh
9. product sorting
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
      categoryFilter: {"95":{"45":[459,467,475,502],"46":[459,467,475]},"96":{"47":[459,467,502,508],"48":[467,502]}},
      currentJQXHR: null,
      refreshIntervalId: null,
    };
  }

  initCategoryFilter() {
    const defaultCategoryFilter = this.state.categoryFilter;
    let categoryFilterCookie = Cookies.get('categoryFilter');
    let categoryFilter = null;
    if (categoryFilterCookie === null || categoryFilterCookie === undefined) {
      categoryFilter = defaultCategoryFilter;
    } else {
      categoryFilter = JSON.parse(categoryFilterCookie);
    }

    this.setState({categoryFilter: categoryFilter});
    Cookies.set('categoryFilter', JSON.stringify(categoryFilter), {path: '/', domain: DOMAIN});
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
	  this.setState({priceFilter, priceFilter});
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
        />
        <PriceFilterModal priceFilter={this.state.priceFilter}
          onPriceFilterChange={(criteria) => this.handlePriceFilterChange(criteria)} />
        {this.state.snapshotProducts != null &&
          <CategoryFilterModal categoryFilter={this.state.categoryFilter}
            snapshotProducts={this.state.snapshotProducts}
            onCategoryFilterChange={(brandId, genderId, categoryId, checked) => this.handleCategoryFilterChange(brandId, genderId, categoryId, checked)} />}
        <Container snapshotProducts={this.state.snapshotProducts}
          priceFilter={this.state.priceFilter}
          categoryFilter={this.state.categoryFilter} />
      </div>
    );
  }
}

ReactDOM.render(<Monitor />, $('#root').get(0));
