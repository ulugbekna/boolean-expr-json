# Boolean Expression JSON (De)Serialization

This package provides classes to represent boolean expressions, which can be serialized to and deserialized from JSON.
`ujson` provides ways to do so -- see `Main` to see how.

### Why `ujson`?

I picked `ujson` because it seems to be fast and compatible with many other Scala JSON libraries.

## Project structure

We have two packages:

- `ujsonImpl` which let `ujson` to do almost everything
- and a more interesting `customUjsonImpl`, which aims at having leaner and more human-readable JSON values. This
  package has some tests.

## Further work

- Tests: testing using fuzzing and property-based checking

  We can create a random generator of `BooleanExpression`s and/or JSON values and test by using the
  properties `serialize(deserialize(json)) = json` (assuming `json` is properly formatted) and
  `deserialize(serialize(boolExpr)) = boolExpr`.

- Had I more time, it could be interesting
    - to write my own parser combinator library and performance optimize it
    - to write a JSON parser either using my own parser library or reusing existings solutions such as the Scala stdlib
      parsing library or `fastparse`

  Serialization seems to be trivial.

- Extend `BooleanExpression` to full propositional logic and maybe first-order logic