import React from 'react';
import { Link } from 'react-router-dom';
import './PublicPages.css';

const content = {
  'how-it-works': ['How it works', 'Create an item, attach its QR code, and let a finder reach you safely if it is lost.'],
  features: ['Features built for recovery', 'Private QR lookup, owner-only item management, voluntary location sharing, and secure finder messages work together to simplify returns.'],
  about: ['Recovery should be simple', 'FindSure makes it easier for people to reconnect with what they have lost while respecting everyone’s privacy.'],
  faq: ['Frequently asked questions', 'FindSure QR codes never place your personal contact information inside the code. Finders can send a message without seeing your private details.'],
  contact: [
    'Contact FindSure',
    <>
      Have a question about using FindSure? Send us a note at{' '}
      <a href="mailto:umeshsinghrathore51@gmail.com">
        umeshsinghrathore51@gmail.com
      </a>.
    </>
  ],
};

const steps = [
  ['01', 'Create your item', 'Save a private record for the belongings you care about.'],
  ['02', 'Attach the QR tag', 'Place your unique code where a finder can scan it.'],
  ['03', 'Reconnect safely', 'Receive a secure message and choose how to respond.'],
];

const features = [
  ['QR identity', 'A simple scan starts a secure return journey.'],
  ['Private by default', 'Your contact details stay out of the QR code.'],
  ['Scan alerts', 'Know when someone has found and scanned your item.'],
  ['Finder messaging', 'Make contact without sharing personal details upfront.'],
];

export function Home() {
  return (
    <main className="home-page">
      <section className="home-hero">
        <div className="shell home-hero-grid">
          <div className="home-hero-copy">
            <p className="eyebrow">QR-powered lost &amp; found</p>
            <h1>Find what matters.<br /><em>Return what matters.</em></h1>
            <p className="lead">Attach a smart QR tag to your belongings. If they’re lost, anyone who finds them can securely help you get them back.</p>
            <div className="home-hero-actions">
              <Link className="button" to="/register">Protect your item <span aria-hidden="true">→</span></Link>
              <Link className="button secondary" to="/how-it-works">How it works</Link>
            </div>
            <p className="home-hero-note"><span aria-hidden="true">✓</span> No personal contact details exposed in your QR code.</p>
          </div>
          <div className="home-hero-visual" aria-label="A FindSure QR tag return journey">
            <div className="home-orbit orbit-one" aria-hidden="true" />
            <div className="home-orbit orbit-two" aria-hidden="true" />
            <div className="home-tag-card">
              <div className="home-tag-top"><span className="home-tag-logo">Find<span>Sure</span></span><span className="home-tag-status">Protected</span></div>
              <div className="home-qr" aria-hidden="true"><i /><i /><i /><i /><i /><i /><i /><i /><i /></div>
              <p>FS-8K29PX</p>
              <small>Scan for a safe return</small>
            </div>
            <div className="home-scan-card"><span aria-hidden="true">✓</span><div><b>Item located</b><small>Owner notified securely</small></div></div>
          </div>
        </div>
      </section>

      <section className="home-trust">
        <div className="shell home-trust-grid">
          <div><p className="eyebrow">Built around trust</p><h2>Helpful for finders.<br />Private for owners.</h2></div>
          <div className="home-trust-points">
            <p><span aria-hidden="true">⌁</span><b>Safe QR identification</b><small>Your tag identifies an item, not your private profile.</small></p>
            <p><span aria-hidden="true">◉</span><b>You stay in control</b><small>Choose if and how you respond when your item is found.</small></p>
            <p><span aria-hidden="true">⌖</span><b>Voluntary location sharing</b><small>Finders share their location only when they choose to.</small></p>
          </div>
        </div>
      </section>

      <section className="section shell home-steps-section">
        <div className="home-section-heading"><div><p className="eyebrow">Simple by design</p><h2>One small tag. A clearer way home.</h2></div><Link to="/how-it-works">See how FindSure works <span aria-hidden="true">→</span></Link></div>
        <div className="home-steps">
          {steps.map(([number, title, description]) => <article className="home-step" key={number}><span>{number}</span><h3>{title}</h3><p>{description}</p></article>)}
        </div>
      </section>

      <section className="home-features-section">
        <div className="shell"><div className="home-section-heading home-features-heading"><div><p className="eyebrow">Made for real life</p><h2>Everything needed for a safer return.</h2></div><p>FindSure keeps the recovery experience simple for the person who lost an item and the person kind enough to find it.</p></div>
          <div className="home-feature-grid">{features.map(([title, description], index) => <article className={`home-feature feature-${index + 1}`} key={title}><span aria-hidden="true">{['⌁', '◌', '◉', '↗'][index]}</span><h3>{title}</h3><p>{description}</p></article>)}</div>
        </div>
      </section>

      <section className="section shell home-cta">
        <div><p className="eyebrow">Ready when you are</p><h2>Give your belongings a better chance of coming back.</h2><p>Set up your first item in minutes and keep the details private.</p></div>
        <Link className="button" to="/register">Get started <span aria-hidden="true">→</span></Link>
      </section>
    </main>
  );
}

export function InfoPage({ type }) {
  const [title, text] = content[type];
  return <main className="section shell prose"><p className="eyebrow">FindSure</p><h1>{title}</h1><p className="lead">{text}</p>{type === 'faq' && <div className="faq"><details><summary>Do finders need an account?</summary><p>No. The public QR page works without an account.</p></details><details><summary>Can a finder share their location?</summary><p>Yes, only after they explicitly choose to do so.</p></details></div>}</main>;
}
