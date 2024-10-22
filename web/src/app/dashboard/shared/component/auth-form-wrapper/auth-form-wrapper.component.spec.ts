import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthFormWrapperComponent } from './auth-form-wrapper.component';

describe('AuthFormWrapperComponent', () => {
  let component: AuthFormWrapperComponent;
  let fixture: ComponentFixture<AuthFormWrapperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthFormWrapperComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AuthFormWrapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
