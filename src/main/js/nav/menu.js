import { SnapshotTimestamp } from './snapshot-timestamp';
import { SnapshotLoading } from './snapshot-loading';
import { ProductSearchBox } from './product-search-box';
import { SnapshotNavigation } from './snapshot-navigation';
import { ProductSearchDropdown } from './product-search-dropdown';

const React = require('react');

class Menu extends React.Component {
  render() {
    return (
      <div className="monitor-menu bg-white border-bottom shadow-sm sticky-top mb-1">
        <ul className="nav flex-column flex-md-row">
          <li className="nav-item my-3">
            <SnapshotNavigation
              currentSnapshotId={this.props.currentSnapshotId}
              onPreviousClick={() => this.props.onPreviousClick()}
              onNextClick={() => this.props.onNextClick()}
              onSnapshotChange={(snapshotId) => this.props.onSnapshotChange(snapshotId)}
              latestSnapshotId={this.props.latestSnapshotId}
            />
          </li>
          <li className="nav-item my-3 position-relative">
            <ProductSearchBox />
            <ProductSearchDropdown />
          </li>
          <li className="nav-item d-flex align-items-center pl-3 my-3 mx-auto mx-lg-0">
            <SnapshotTimestamp snapshotTimestamp={this.props.snapshotTimestamp} />
            <SnapshotLoading isLoading={this.props.isLoading} />
          </li>
        </ul>
      </div>
    );
  }
}

export { Menu };
