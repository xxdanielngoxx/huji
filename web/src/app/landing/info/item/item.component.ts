import { Component, Input } from '@angular/core';
import {
  FontAwesomeModule,
  IconDefinition,
} from '@fortawesome/angular-fontawesome';
import { faInfo } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-info-item',
  standalone: true,
  imports: [FontAwesomeModule],
  templateUrl: './item.component.html',
  styleUrl: './item.component.scss',
})
export class ItemComponent {
  @Input() propertyName? = '';
  @Input() value? = '';
  @Input() icon?: IconDefinition;

  defaultIcon: IconDefinition = faInfo;
}
