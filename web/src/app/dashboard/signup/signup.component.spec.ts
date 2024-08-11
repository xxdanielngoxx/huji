import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SignupComponent } from './signup.component';
import { SignupFormComponent } from './signup-form/signup-form.component';
import { Component } from '@angular/core';
import { provideRouter } from '@angular/router';

@Component({
  selector: 'app-signup-form',
  template: '',
  standalone: true,
})
class SignupFormMockComponent {}

describe('SignupComponent', () => {
  let component: SignupComponent;
  let fixture: ComponentFixture<SignupComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SignupComponent],
      providers: [provideRouter([])],
    })
      .overrideComponent(SignupComponent, {
        remove: { imports: [SignupFormComponent] },
        add: { imports: [SignupFormMockComponent] },
      })
      .compileComponents();

    fixture = TestBed.createComponent(SignupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
